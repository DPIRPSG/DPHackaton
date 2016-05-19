package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ActorRepository actorRepository;
	
	// Supporting services ----------------------------------------------------
	
	// Constructors -----------------------------------------------------------
	
	public ActorService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	public Collection<Actor> findAll(){
		Collection<Actor> result;
		
		result = actorRepository.findAll();
		
		return result;
	}
	
	public Actor findOne(int actorId){
		Actor result;
		
		result = actorRepository.findOne(actorId);
		
		return result;
	}
	
	// Other business methods -------------------------------------------------7
	
	private Actor save(Actor actor){
		Assert.isTrue(this.checkLogin(), "Only an Actor can change his/her data.");
		Assert.notNull(actor);
		
		Actor result;
		Actor principalActor;
		
		principalActor = this.findByPrincipal();
		
		Assert.isTrue(principalActor.getId() == actor.getId(), "You can't modify other user's data.");
		
		result = actorRepository.save(actor);
		
		return result;
	}
	
	public Actor saveCurriculum(Actor actor){
		Assert.isTrue(this.checkAuthorities("RUNNER,MANAGER,REFEREE"), "Only a Runner, Manager or Referee can manage his/her Curriculum.");
		
		Actor result;
		
		result = this.save(actor);
		
		return result;
	}

	/**
	 *  Devuelve el actor que está realizando la operación
	 */
	//req: 24.1, 24.2
	public Actor findByPrincipal(){
		Actor result;
		UserAccount userAccount;
		
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = actorRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);
		
		return result;
	}

	/**
	 * Comprueba si el usuario que está ejecutando tiene la AuthoritySolicitada
	 * @return boolean -> false si no es customer
	 * @param authority [ADMIN, CUSTOMER]
	 */
	public boolean checkAuthority(String authority){
		boolean result;
		Actor actor;
		Collection<Authority> authorities;
		result = false;

		try {
			actor = this.findByPrincipal();
			authorities = actor.getUserAccount().getAuthorities();
			
			for (Authority a : authorities) {
				if(a.getAuthority().equals(authority.toUpperCase())){
					result = true;
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			result = false;
		}
		return result;
	}
	
	/**
	 * Comprueba si el usuario que está ejecutando tiene alguna de las AuthoritiesSolicitadas
	 * Deben introducirse separados por ","
	 * @return boolean -> false si no es customer
	 * @param authority [ADMIN, CUSTOMER]
	 */
	public boolean checkAuthorities(String authoritiesInput){
		boolean result;
		result = false;

		for(String a:authoritiesInput.split(",")){
			if(this.checkAuthority(a.trim())){
				result = true;
				break;
			}
		}

		return result;
	}
	
	/**
	 * Comprueba si un usuario está autenticado
	 */
	public boolean checkLogin(){
		boolean result;
		
		result = true;
		
		try{
			this.findByPrincipal();
		} catch (Throwable e) {
			result = false;
		}
		return result;
	}
	
	public Actor findByCurriculumId(int actorId){
		Actor result;
		
		result = actorRepository.findByCurriculumId(actorId);
		
		return result;
	}
	
}
