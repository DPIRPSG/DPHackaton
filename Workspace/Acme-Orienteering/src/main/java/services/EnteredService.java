package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Club;
import domain.Entered;
import domain.Runner;

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
	
	@Autowired
	private RunnerService runnerService;

	// Constructors -----------------------------------------------------------
	
	public EnteredService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	/**
	 * 
	 * @param clubId
	 * @see 21.a
	 * 	Un usuario que haya iniciado sesión como corredor debe poder:
	 * 	Hacer peticiones de ingreso a los distintos clubes del sistema.
	 * @return create an entered to a club from the runner logged
	 */
	public Entered create(){
		
		Assert.isTrue(actorService.checkAuthority("RUNNER"), "Only a runner can create an entered.");
		
		Entered result;
		Runner runner;
		
		runner = runnerService.findByPrincipal();
		
		result = new Entered();
		
		result.setRunner(runner);
		
		result.setIsMember(false);
		result.setRegisterMoment(new Date());
		
		return result;
	}
	
	/**
	 * 
	 * @param entered
	 * @see 21.a
	 * 	Un usuario que haya iniciado sesión como corredor debe poder:
	 * 	Hacer peticiones de ingreso a los distintos clubes del sistema.
	 * @return the entered saved in the database
	 */
	public Entered save(Entered entered){
		
		Assert.notNull(entered);
		Assert.isTrue(actorService.checkAuthority("RUNNER"), "Only a runner can save a entered.");
		
		enteredRepository.save(entered);
		
		return entered;
	}
	
	/**
	 * 
	 * @param entered
	 * @see 22.d
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Aceptar o denegar las peticiones.
	 */
	public void accept(Entered entered){
		
		Assert.notNull(entered);
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can accept a entered");
		
		Runner runner;
		Collection<Entered> runnerEntereds;
		Collection<Club> runnerClub = new HashSet<>();
		
		runner = entered.getRunner();
		runnerEntereds = enteredRepository.findAllByRunner(runner.getId());
		
		for(Entered e:runnerEntereds){
			if(e.getIsMember() == true){
				runnerClub.add(e.getClub());
				break;
			}
		}
		Assert.isTrue(runnerClub.isEmpty(), "You cannot accept a runner who is in another club.");
		
		entered.setIsMember(true);
		entered.setAcceptedMoment(new Date());
		enteredRepository.save(entered);
		
	}
	
	/**
	 * 
	 * @param entered
	 * @see 22.d
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Aceptar o denegar las peticiones.
	 */
	public void deny(Entered entered){
		
		Assert.notNull(entered);
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can accept a entered");
		Assert.isTrue(entered.getIsMember() == false, "You can only deny an unaccepted entered");
		Assert.isNull(entered.getAcceptedMoment(), "You can only deny an unaccepted entered");
		
		enteredRepository.delete(entered);
	}
		
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
	
	/**
	 * 
	 * @param clubId
	 * @return the collection of accepted entered that a club have.
	 */
	public Collection<Entered> findAllAcceptedByClub(int clubId){
		Assert.isTrue(actorService.checkAuthority("MANAGER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllAcceptedByClub(clubId);
		
		return result;
	}
	
	/**
	 * 
	 * @param clubId
	 * @return the collection of denied entered that a club have.
	 */
	public Collection<Entered> findAllDeniedByClub(int clubId){
		Assert.isTrue(actorService.checkAuthority("MANAGER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllDeniedByClub(clubId);
		
		return result;
	}
	
	/**
	 * 
	 * @param clubId
	 * @return the collection of expelled entered that a club have.
	 */
	public Collection<Entered> findAllExpelledByClub(int clubId){
		Assert.isTrue(actorService.checkAuthority("MANAGER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllExpelledByClub(clubId);
		
		return result;
	}
	
	public Entered findOne(int enteredId){
		Entered result;
		
		result = enteredRepository.findOne(enteredId);
		
		return result;
	}
	
	public void flush(){
		enteredRepository.flush();
	}
}
