package services;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Runner;
import repositories.RunnerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class RunnerService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private RunnerRepository runnerRepository;
	
	//Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	//Constructors -----------------------------------------------------------

	public RunnerService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	/** Devuelve customer preparado para ser modificado. Necesita usar save para que persista en la base de datos
	 * 
	 */
	// req: 10.1
	public Runner create(){
		Runner result;
		UserAccount userAccount;

		result = new Runner();
		
		userAccount = userAccountService.create("RUNNER");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	/**
	 * Almacena en la base de datos el cambio
	 */
	// req: 10.1
	private void save(Runner runner){
		Assert.notNull(runner);
		
		boolean result = true;
		for(Authority a: runner.getUserAccount().getAuthorities()){
			if(!a.getAuthority().equals("RUNNER")){
				result = false;
				break;
			}
		}
		Assert.isTrue(result, "A runner can only be a authority.runner");
		
		runnerRepository.save(runner);
	}
	
	/**
	 *  Almacena en la base de datos un cambio realizado desde el formulario de edición 
	 */
	public void saveFromEdit(Runner runner){
		
		Assert.isTrue(
				actorService.checkAuthority("RUNNER")
						|| (!actorService.checkLogin() && runner.getId() == 0),
						"RunnerService.saveFromEdit.permissionDenied");
		if(runner.getId() == 0){ //First save
			UserAccount auth;
			
			//Encoding password
			auth = runner.getUserAccount();
			auth = userAccountService.modifyPassword(auth);
			runner.setUserAccount(auth);
			
		}
		this.save(runner);
	}
	
	/**
	 * Lista los customers registrados
	 */
	// req: 12.5
	public Collection<Runner> findAll(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can list customers");
		
		Collection<Runner> result;
		
		result = runnerRepository.findAll();
		
		return result;
	}

	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el customers que está realizando la operación
	 */
	//req: x
	public Runner findByPrincipal(){
		Runner result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = runnerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}

	public Collection<Runner> findAllByClubId(int clubId) {
		Collection<Runner> result;
		
		result = runnerRepository.findAllByClubId(clubId);
		
		return result;
	}
	
	public void flush(){
		runnerRepository.flush();
	}
	

}
