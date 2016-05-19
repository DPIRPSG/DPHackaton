package services;


import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Club;
import domain.Comment;
import domain.Entered;
import domain.MessageEntity;
import domain.Participates;
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
	public Runner create(){
		Runner result;
		UserAccount userAccount;
		Collection<Comment> comments;
		Collection<MessageEntity> messages, messages2;
		Collection<Entered> entered;
		Collection<Participates> participates;

		comments = new ArrayList<Comment>();
		messages = new ArrayList<MessageEntity>();
		messages2 = new ArrayList<MessageEntity>();
		entered = new ArrayList<Entered>();
		participates = new ArrayList<Participates>();

		result = new Runner();
		result.setComments(comments);
		result.setReceived(messages2);
		result.setSent(messages);
		result.setEntered(entered);
		result.setParticipates(participates);
		
		userAccount = userAccountService.create("RUNNER");
		result.setUserAccount(userAccount);
		
		return result;
	}
	
	/**
	 * Almacena en la base de datos el cambio
	 */
	private Runner save(Runner runner){
		Assert.notNull(runner);
		Runner saved;
		
		boolean result = false;
		for(Authority a: runner.getUserAccount().getAuthorities()){
			if(a.getAuthority().equals("RUNNER")){
				result = true;
			}else{
				result = false;
				break;
			}
		}
		Assert.isTrue(result, "A runner can only be a authority.runner");
		
		saved = runnerRepository.save(runner);
			
		return saved;
	}
	
	/**
	 *  Almacena en la base de datos un cambio realizado desde el formulario de edición 
	 */
	public Runner saveFromEdit(Runner runner){
		Runner result;
		
		Assert.isTrue(
				actorService.checkAuthority("RUNNER")
						|| (!actorService.checkLogin() && runner.getId() == 0),
						"RunnerService.saveFromEdit.permissionDenied");

		result = this.save(runner);
		
		return result;
	}
	
	/**
	 * Lista los customers registrados
	 */
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
	
	public Club getClub(){
		Club res;
		Runner runner;
		
		runner = this.findByPrincipal();
		res = this.getClub(runner);
				
		return res;
	}

	public Club getClub(Runner input){
		Club res;
		
		res = null;
		for(Entered e:input.getEntered()){
			if(e.getIsMember() && e.getAcceptedMoment() != null)
				res = e.getClub();
		}
				
		return res;
	}
	
	public void flush(){
		runnerRepository.flush();
	}
	

}
