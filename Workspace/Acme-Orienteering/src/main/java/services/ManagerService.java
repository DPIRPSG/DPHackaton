package services;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Comment;
import domain.Manager;
import domain.MessageEntity;
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
		Collection<Comment> comments;
		Collection<MessageEntity> messages;

		comments = new ArrayList<Comment>();
		messages = new ArrayList<MessageEntity>();

		result = new Manager();
		result.setComments(comments);
		result.setReceived(messages);
		result.setSent(messages);
		
		userAccount = userAccountService.create("MANAGER");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	private Manager save(Manager manager) {
		Assert.notNull(manager);
		
		boolean result = false;
		for(Authority a: manager.getUserAccount().getAuthorities()){
			if(a.getAuthority().equals("MANAGER")){
				result = true;
			}else{
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
		
		result = this.save(manager);
		
		return result;
	}
	
	public void saveFromOthers(Manager manager){
		Assert.isTrue(actorService.checkLogin(),"ManagerService.saveFromOthers.permissionDenied");
		this.save(manager);
	}
	
	/**
	 * Necesario para los test
	 * @return
	 */
	public Collection<Manager> findAll(){
		Assert.isTrue(actorService.checkAuthority("ADMIN"));
		Collection<Manager> result;
		
		result = managerRepository.findAll();
		
		return result;
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

	public Manager findOne(int managerId) {
		Manager result;
		
		result = managerRepository.findOne(managerId);
		
		return result;
	}

	public Collection<Manager> findAllWithoutClub() {
		Collection<Manager> result, managers;
		
		managers = managerRepository.findAll();
		result = new ArrayList<Manager>();
		
		for(Manager m : managers) {
			if(m.getClub() == null) {
				result.add(m);
			}
		}
		
		return result;
	}
	

}
