package services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Referee;
import repositories.RefereeRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class RefereeService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private RefereeRepository refereeRepository;

	//Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	//Constructors -----------------------------------------------------------

	public RefereeService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------

	public Referee create(){
		Referee result;
		UserAccount userAccount;

		result = new Referee();
		
		userAccount = userAccountService.create("REFEREE");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	private void save(Referee input) {
		Assert.notNull(input);
		
		boolean result = true;
		for(Authority a: input.getUserAccount().getAuthorities()){
			if(!a.getAuthority().equals("REFEREE")){
				result = false;
				break;
			}
		}
		Assert.isTrue(result, "A referee can only be a authority.referee");
				
		input = refereeRepository.save(input);
	}
	
	public void saveFromEdit(Referee manager){
		Assert.isTrue(actorService.checkAuthority("REFEREE")
						|| (actorService.checkAuthority("ADMIN") && manager.getId() == 0),
						"RefereeService.saveFromEdit.permissionDenied");
		if(manager.getId() == 0){ //First save
			UserAccount auth;
			
			//Encoding password
			auth = manager.getUserAccount();
			auth = userAccountService.modifyPassword(auth);
			manager.setUserAccount(auth);
		}
		this.save(manager);
	}
	
	public void saveFromOthers(Referee manager){
		Assert.isTrue(actorService.checkLogin(),"RefereeService.saveFromOthers.permissionDenied");
		this.save(manager);
	}
	
	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el manager que está realizando la operación
	 */
	//req: x
	public Referee findByPrincipal(){
		Referee result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = refereeRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}
	

}
