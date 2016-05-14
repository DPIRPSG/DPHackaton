package services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Manager;
import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class ManagerService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private ManagerRepository managerRepository;

	//Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	//Constructors -----------------------------------------------------------

	public ManagerService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------

	public Manager create(){
		Manager result;
		UserAccount userAccount;

		result = new Manager();
		
		userAccount = userAccountService.create("MANAGER");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	private Manager save(Manager manager) {
		Assert.notNull(manager);
		
		boolean result = true;
		for(Authority a: manager.getUserAccount().getAuthorities()){
			if(!a.getAuthority().equals("MANAGER")){
				result = false;
				break;
			}
		}
		Assert.isTrue(result, "A manager can only be a authority.manager");
		
		manager = managerRepository.save(manager);
		
		return manager;
	}
	
	public Manager saveFromEdit(Manager manager){
		Assert.isTrue(
				actorService.checkAuthority("MANAGER")
						|| (actorService.checkAuthority("ADMIN") && manager.getId() == 0),
						"ManagerService.saveFromEdit.permissionDenied");
		Manager result;
		
//		if(manager.getId() == 0){ //First save
//			UserAccount auth;
//			
//			//Encoding password
//			auth = manager.getUserAccount();
//			auth = userAccountService.modifyPassword(auth);
//			manager.setUserAccount(auth);
//		}
		
		result = this.save(manager);
		
		return result;
	}
	
	public void saveFromOthers(Manager manager){
		Assert.isTrue(actorService.checkLogin(),"ManagerService.saveFromOthers.permissionDenied");
		this.save(manager);
	}
	
	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el manager que está realizando la operación
	 */
	//req: x
	public Manager findByPrincipal(){
		Manager result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = managerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}
	

}
