package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Entered;

import repositories.EnteredRepository;

@Service
@Transactional
public class EnteredService {
	
	// Managed repository -----------------------------------------------------
	
	@Autowired
	private EnteredRepository enteredRepository;
	
	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------
	
	public EnteredService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	/**
	 * 
	 * @param runnerId
	 * @see 21.b
	 * 	Un usuario que haya iniciado sesión como corredor debe poder:
	 * 	Revisar el estado de las distintas peticiones que haya realizado.
	 * @return the collection of entered done by a runner
	 */
	public Collection<Entered> findAllByRunner(int runnerId){
		Assert.isTrue(actorService.checkAuthority("RUNNER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllByRunner(runnerId);
		
		return result;
	}
	
	/**
	 * 
	 * @param clubId
	 * @see 22.c
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Revisar las distintas peticiones de ingreso que le han hecho al club.
	 * @return the collection of entered that a club receive.
	 */
	public Collection<Entered> findAllByClub(int clubId){
		Assert.isTrue(actorService.checkAuthority("MANAGER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllByClub(clubId);
		
		return result;
	}
	
	/**
	 * 
	 * @param clubId
	 * @see
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Aceptar o denegar las peticiones.
	 * @return the collection of entered not accepted yet that a club have received.
	 */
	public Collection<Entered> findAllUnresolvedByClub(int clubId){
		Assert.isTrue(actorService.checkAuthority("MANAGER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllUnresolvedByClub(clubId);
		
		return result;
	}

	public void flush(){
		enteredRepository.flush();
	}
}
