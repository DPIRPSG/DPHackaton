package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Folder;
import domain.Message;
import domain.Auditor;


import repositories.AuditorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;

@Service
@Transactional
public class AuditorService {
	//Managed repository -----------------------------------------------------
	
	@Autowired
	private AuditorRepository auditorRepository;
	
	//Supporting services ----------------------------------------------------

	@Autowired
	private FolderService folderService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserAccountService userAccountService;
	
	//Constructors -----------------------------------------------------------

	public AuditorService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	/** Devuelve trainer preparado para ser modificado. Necesita usar save para que persista en la base de datos
	 * 
	 */
	// req: 10.1
	public Auditor create(){
		Auditor result;
		UserAccount userAccount;

		result = new Auditor();
		
		userAccount = userAccountService.create("AUDITOR");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	/**
	 * Almacena en la base de datos el cambio
	 */
	// req: 10.1
	public Auditor save(Auditor trainer){
		Assert.notNull(trainer);
		Assert.notNull(trainer.getUserAccount().getUsername());
		Assert.notNull(trainer.getUserAccount().getPassword());
		
		Auditor modify;
		
		boolean result = true;
		for(Authority a: trainer.getUserAccount().getAuthorities()){
			if(!a.getAuthority().equals("AUDITOR")){
				result = false;
				break;
			}
		}
		Assert.isTrue(result, "A auditor can only be a authority.auditor");
		
		if(trainer.getId() == 0){
			Assert.isTrue(actorService.checkAuthority("ADMIN"), "auditor.create.permissionDenied");
			
			Collection<Folder> folders;
			Collection<Message> sent;
			Collection<Message> received;
			UserAccount auth;
			
			//Encoding password
			auth = trainer.getUserAccount();
			auth = userAccountService.modifyPassword(auth);
			trainer.setUserAccount(auth);
			
			// Initialize folders
			folders = folderService.initializeSystemFolder(trainer);
			trainer.setMessageBoxes(folders);
			
			sent = new ArrayList<Message>();
			received = new ArrayList<Message>();
			trainer.setSent(sent);
			trainer.setReceived(received);
			
			// Initialize anothers
			
		}
		//modify = customerRepository.saveAndFlush(customer);
		modify = auditorRepository.save(trainer);		
		
		if(trainer.getId() == 0){
			Collection<Folder> folders;

			folders = folderService.initializeSystemFolder(modify);
			folderService.save(folders);
		}
		return modify;
	}
	
	
	
	public Collection<Auditor> findAll(){
		Collection<Auditor> result;
		
		result = auditorRepository.findAll();
		
		return result;
	}
	
	public Auditor findOne(int id){
		Auditor result;
		
		result = auditorRepository.findOne(id);
		
		return result;
	}
	

	//Other business methods -------------------------------------------------

	/**
	 * Devuelve el trainer que está realizando la operación
	 */
	//req: x
	public Auditor findByPrincipal(){
		Auditor result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = auditorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}
	
	public void flush(){
		auditorRepository.flush();
	}

	public Collection<Auditor> getAuditorsWithMoreMatches(){
		Collection<Auditor> result;
		
		result = auditorRepository.getAuditorsWithMoreMatches();
		
		return result;
	}
}
