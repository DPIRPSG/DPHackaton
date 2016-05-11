package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ComplaintRepository;
import domain.Barter;
import domain.Complaint;
import domain.Match;
import domain.User;

@Service
@Transactional
public class ComplaintService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ComplaintRepository complaintRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BarterService barterService;
	
	@Autowired
	private MatchService matchService;
	
	
	// Constructors -----------------------------------------------------------

	public ComplaintService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	
	public Complaint create(int barterOrMatchId){
		
		Assert.isTrue(actorService.checkAuthority("USER"), "Only an User can create a Complaint.");
		
		Complaint result;
		User user;
		
		user = userService.findByPrincipal();
		
		result = new Complaint();
		
		result.setUser(user);
		
		Barter barter;
		
		barter = barterService.findOne(barterOrMatchId);
		
		if(barter != null){
			result.setBarter(barter);
		}else{
			
			Match match;
			
			match = matchService.findOne(barterOrMatchId);
			
			Assert.notNull(match, "You can't create a Complaint without a Barter or a Match asociated.");
			
			result.setMatch(match);
			
		}
		
		
		return result;
		
	}
	
	public Complaint save(Complaint complaint){
		
		Assert.isTrue(actorService.checkAuthority("USER"), "Only an User can manage a Complaint.");
		Assert.isTrue( (complaint.getBarter() != null && complaint.getMatch() == null) || (complaint.getBarter() == null && complaint.getMatch() != null), "You can only create a Complaint to a Barter or a Match, not both.");
		
		Assert.notNull(complaint);
		
		User user;
		Collection<Match> matchesInvolved;
		
		user = userService.findByPrincipal();
		
		matchesInvolved = matchService.findAllUserInvolvesIncludeCancelled(user.getId());
		
		if(complaint.getMatch() != null){ // Si está siendo creado para un Match
			Assert.isTrue(matchesInvolved.contains(complaint.getMatch()), "You can't create a Complaint to a Match in which you aren't involved.");
			Assert.isTrue(!complaint.getMatch().getClosed(), "You can't create a Complaint for a closed Match.");
		}else{ // Si está siendo creado para un Barter
			Assert.isTrue(complaint.getBarter().getUser() != user, "You can't create a Complaint for your Barters.");
			Assert.isTrue(!complaint.getBarter().getClosed(), "You can't create a Complaint for a closed Barter.");
		}
		
		complaint.setUser(user);
		
		complaint = complaintRepository.save(complaint);
		
		return complaint;
	}
	
	public Complaint findOne(int complaintId) {
		Complaint result;

		result = complaintRepository.findOne(complaintId);

		return result;
	}
	
	public Collection<Complaint> findAll() {
		Collection<Complaint> result;
		
		result = complaintRepository.findAll();
		
		return result;
	}
	
	public Collection<Complaint> findAllByBarterOrMatch(int barterOrMatchId) {
		Collection<Complaint> result;
		
		result = complaintRepository.findAllByBarterOrMatch(barterOrMatchId);
		
		return result;
	}
	
	public Integer getTotalNumberOfComplaintsCreated(){
		Integer result;
		
		result = complaintRepository.getTotalNumberOfComplaintsCreated();
		
		return result;
	}
	
	public Collection<Complaint> findByUser(User u){
		Collection<Complaint> result;
		
		result = complaintRepository.findAllByUserId(u.getId());
		
		return result;
	}
	
}
