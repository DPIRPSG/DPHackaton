package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.BarterRepository;
import domain.Administrator;
import domain.Barter;
import domain.Match;
import domain.User;

@Service
@Transactional
public class BarterService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private BarterRepository barterRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService userService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private AdministratorService administratorService;
	
	// Constructors -----------------------------------------------------------

	public BarterService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Barter findOne(int barterId) {
		Barter result;

		result = barterRepository.findOne(barterId);

		return result;
	}

	public Collection<Barter> findAll() {
		Collection<Barter> result;

		result = barterRepository.findAll();

		return result;
	}

	// Other business methods -------------------------------------------------

	public void flush() {
		barterRepository.flush();
	}

	public Collection<Barter> findAllNotCancelled() {
		Collection<Barter> result;

		result = barterRepository.findAllNotCancelled();

		return result;
	}

	public Collection<Barter> findBySingleKeywordNotCancelled(String keyword) {
		Assert.notNull(keyword);
		Assert.isTrue(!keyword.isEmpty());
		
		Collection<Barter> result;

		result = barterRepository.findBySingleKeywordNotCancelled(keyword);
		
		return result;
	}
	
	public Collection<Barter> findBySingleKeyword(String keyword) {
		Assert.notNull(keyword);
		Assert.isTrue(!keyword.isEmpty());
		
		Collection<Barter> result;

		result = barterRepository.findBySingleKeyword(keyword);
		
		return result;
	}
	
	public Collection<Barter> findByUserNotCancelled(int userId){
		Collection<Barter> result;
		
		result = barterRepository.findByUserIdNotCancelled(userId);
		
		return result;
	}
	
	public Collection<Barter> findByUserIdNotCancelledNotInMatchNotCancelled(int userId){
		Collection<Barter> result;
		
		result = barterRepository.findByUserIdNotCancelledNotInMatchNotCancelled(userId);
		
		return result;
	}
	
	public Collection<Barter> findAllOfOtherUsersByUserIdNotCancelledNotInMatchNotCancelled(int userId){
		Collection<Barter> result;
		
		result = barterRepository.findAllOfOtherUsersByUserIdNotCancelledNotInMatchNotCancelled(userId);
		
		return result;
	}

	public Collection<Barter> findAllByFollowedUser() {
		Collection<Barter> result;
		User user;
		
		user = userService.findByPrincipal();
		
		result = barterRepository.findAllByFollowedUser(user.getId());
		
		return result;
	}
	
	public Collection<Barter> findAllNotRelated(int barterId){
		Collection<Barter> result;
		Barter actualBarter;
		
		actualBarter = this.findOne(barterId);
		result = this.findAll();
		result.remove(actualBarter);
		result.removeAll(this.getRelatedBarters(barterId));
		
		return result;
	}
	
	public Barter create(){
		
		Assert.isTrue(actorService.checkAuthority("USER"), "Only a user can create a barter");
		
		Barter barter;
		Collection<Match> createdMatch;
		Collection<Match> receivedMatch;
		Collection<Barter> relatedBarter;
		
		barter = new Barter();
		createdMatch = new ArrayList<>();
		receivedMatch = new ArrayList<>();
		relatedBarter = new ArrayList<>();
		
		barter.setCreatedMatch(createdMatch);
		barter.setReceivedMatch(receivedMatch);
		barter.setRelatedBarter(relatedBarter);
		barter.setClosed(false);
		
		return barter;
		
	}
	
	public Barter save(Barter barter){
		
		Assert.notNull(barter);
		Barter result;
		
		result = barterRepository.save(barter);
		
		return result;
	}
	
	public Barter saveToEdit(Barter barter){
		
		Assert.notNull(barter);
		Assert.isTrue(actorService.checkAuthority("USER") || actorService.checkAuthority("ADMIN"), "Only a user or an admin can save a barter");
		
		if(barter.getId() == 0){
			User user;
			Collection<Match> createdMatch;
			Collection<Match> receivedMatch;
			Collection<Barter> relatedBarter;
			
			user = userService.findByPrincipal();
			createdMatch = new ArrayList<>();
			receivedMatch = new ArrayList<>();
			relatedBarter = new ArrayList<>();
			
			barter.setCancelled(false);
			barter.setClosed(false);
			barter.setRegisterMoment(new Date());
			
			barter.setUser(user);
			barter.setCreatedMatch(createdMatch);
			barter.setReceivedMatch(receivedMatch);
			barter.setRelatedBarter(relatedBarter);
			barter = this.save(barter);
		}else{
			Barter barterPreSave;
			barterPreSave = this.findOne(barter.getId());
			barterPreSave.setRelatedBarter(barter.getRelatedBarter());
			barter = this.save(barterPreSave);
		}
		
		return barter;
	}
	
	public Barter saveToRelate(Barter input){
		Assert.notNull(input);
		Assert.isTrue(input.getId() > 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can relate a barter");

		Barter result, result2;
		Collection<Barter> oldContain, toSave;
		
		result = this.findOne(input.getId());
		Assert.notNull(result);
		
		toSave = new HashSet<Barter>(result.getRelatedBarter());
		oldContain = this.getRelatedBarters(input.getId());
		
//		Solo los relacionaremos en una dirección
		
		for(Barter related:input.getRelatedBarter()){
			if(!(oldContain.contains(related) || toSave.contains(related)) && related != null){
				toSave.add(related);
			}
		}
		result.setRelatedBarter(toSave);
		
		result2 = this.save(result);
		
		return result2;
	}
	
	public void cancel(Barter barter){
		
		Assert.notNull(barter);
		Assert.isTrue(barter.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN") || actorService.checkAuthority("USER"), "Only an administrator or a user can cancel a barter.");
		Assert.isTrue(!barter.isCancelled(), "This barter is already deleted.");
		
		if(actorService.checkAuthority("USER")){
			Assert.isTrue(barter.getUser().equals(userService.findByPrincipal()));
		}
		
		barter.setCancelled(true);	
		
		barterRepository.save(barter);
		
	}
	
	public void close(Barter barter) {
		
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an Admin loged in into the system can cancel a Barter.");
		
		Assert.notNull(barter);
		
		Assert.isTrue(!barter.getClosed(), "This barter is already closed!");
		
		Administrator admin;
		
		admin = administratorService.findByPrincipal();
		
		barter.setClosed(true);
		barter.setAdministrator(admin);
		
		this.save(barter);
		
	}
	
	//DASHBOARD
	public Integer getTotalNumberOfBarterRegistered(){
		Integer result;
		
		result = barterRepository.getTotalNumberOfBarterRegistered();
		
		return result;
	}
	
	public Integer getTotalNumberOfBarterCancelled(){
		Integer result;
		
		result = barterRepository.getTotalNumberOfBarterCancelled();
		
		return result;
	}
	
	public Double ratioBarterNotRelatedToAnyOtherBarter(){
		Double result;
		Collection<Barter> allBarter = new HashSet<>();
		Collection<Barter> allBarter2 = new HashSet<>();
		Integer numerator;
		Integer denominator;
		
		allBarter = findAll();
		denominator = allBarter.size();
				
		for(Barter b:allBarter){
			for(Barter b2:b.getRelatedBarter()){
				allBarter2.add(b2);
			}
		}
		allBarter.removeAll(allBarter2);
		numerator = allBarter.size();
		
		if(denominator == 0){
			denominator = 1;
		}
				
		result =  (numerator.doubleValue() / denominator.doubleValue());
				
		return result;
	}
	
	public Collection<Barter> getRelatedBarters(int barterId){
		Collection<Barter> result, toRemove;
		Barter b;
		
//		result = barterRepository.getOtherRelatedBartersById(barterId);
		result = new HashSet<Barter>(barterRepository.getOtherRelatedBartersById(barterId));
		toRemove = new ArrayList<Barter>(result);
		
		b = this.findOne(barterId);
		toRemove.retainAll(b.getRelatedBarter());
		
		result.removeAll(toRemove);
		result.addAll(b.getRelatedBarter());

		return result;
	}

	public Barter findOneByItemId(int itemId) {
		Barter result;
		
		result = barterRepository.findOneByItemId(itemId);
		
		return result;
	}
	
	public Double getAverageOfComplaintsPerBarter(){
		Double result = 0.0;
		Collection<Long> count;
		Long totalCount = 0L;
		
		count = barterRepository.getCountOfComplaintsPerBarter();
		
		for(Long l:count){
			totalCount += l;
		}
		
		result = totalCount.doubleValue();
		if(count.size() != 0){
			result = result / count.size();
		}else{
			result = 0.0;
		}
		
		return result;
		
	}
}
