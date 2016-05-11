package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.User;
import domain.SocialIdentity;

import repositories.SocialIdentityRepository;

@Service
@Transactional
public class SocialIdentityService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private SocialIdentityRepository socialIdentityRepository;
	
	//Supporting services ----------------------------------------------------
	
	@Autowired
	private UserService customerService;
	
	//Constructors -----------------------------------------------------------

	public SocialIdentityService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	/**
	 * Almacena en la base de datos el cambio
	 */
	private void save(SocialIdentity socialIdentity){
		Assert.notNull(socialIdentity);
				
		socialIdentity = socialIdentityRepository.save(socialIdentity);
		
	}
	
	private SocialIdentity create(){
		SocialIdentity result;
		User user;
		
		user = customerService.findByPrincipal();
		result = new SocialIdentity();
		result.setUser(user);
		
		return result;
	}
	
	private void delete(SocialIdentity input){
		Assert.notNull(input);
		
		socialIdentityRepository.delete(input);
	}
	
	//Other business methods -------------------------------------------------
	/**
	 * Almacena en la base de datos el cambio desde la edición
	 */
	public void saveFromEdit(SocialIdentity socialIdentity){
		checkProperty(socialIdentity);
		
		this.save(socialIdentity);
	}	
	
	public void deleteFromEdit(SocialIdentity input){
		checkProperty(input);
		
		this.delete(input);
	}
	
	public Collection<SocialIdentity> findByPrincipal(){
		Collection<SocialIdentity> result;
		User custo;
		
		custo = customerService.findByPrincipal();
		result = this.findByUserId(custo.getId());
		
		return result;
	}
	
	public Collection<SocialIdentity> findByUserId(int userId){
		Collection<SocialIdentity> result;
		
		result = socialIdentityRepository.findByUserId(userId);
		
		return result;
	}
	
	public SocialIdentity findOrCreateById(String socialIdentityId){
		SocialIdentity result;
		
		result = null;
		
		if(! socialIdentityId.equals(""))
		result = socialIdentityRepository.findOne(Integer.decode(
				socialIdentityId).intValue());
		
		
		if(result == null)
			result = this.create();

		checkProperty(result);
		return result;
	}
	
	private void checkProperty(SocialIdentity input){
		Assert.isTrue(input.getUser().equals(customerService.findByPrincipal()), "socialIdentity.NotProperty");

	}

}
