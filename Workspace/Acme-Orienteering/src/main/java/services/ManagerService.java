package services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Manager;
import repositories.ManagerRepository;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ManagerService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private ManagerRepository managerRepository;
	
	//Supporting services ----------------------------------------------------
	
	//Constructors -----------------------------------------------------------

	public ManagerService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------

	public void save(Manager manager) {
		Assert.notNull(manager);
		
		manager = managerRepository.save(manager);
	}
	
	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el customers que está realizando la operación
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
