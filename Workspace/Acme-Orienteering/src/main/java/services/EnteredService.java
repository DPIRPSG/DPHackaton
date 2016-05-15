package services;

import java.util.Collection;
import java.util.Date;

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
	
	@Autowired
	private ClubService clubService;

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
	public Entered create(int clubId){
		
		Assert.isTrue(actorService.checkAuthority("RUNNER"), "Only a runner can create an entered");
		
		Entered result;
		Runner runner;
		Club club;
		
		runner = runnerService.findByPrincipal();
		club = clubService.findOne(clubId);
		
		result = new Entered();
		
		result.setRunner(runner);
		result.setClub(club);
		
		result.setIsMember(false);
		result.setRegisterMoment(new Date());
		
		return result;
	}
	
	public Entered save(Entered entered){
		
		Assert.notNull(entered);
		Assert.isTrue(actorService.checkAuthorities("RUNNER,MANAGER"), "Only a runner or a manager can save a entered");
		
		enteredRepository.save(entered);
		
		return entered;
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
	 * @return the collection of expelled entered that a club have.
	 */
	public Collection<Entered> findAllExpelledByClub(int clubId){
		Assert.isTrue(actorService.checkAuthority("MANAGER"));
		
		Collection<Entered> result;
		
		result = enteredRepository.findAllExpelledByClub(clubId);
		
		return result;
	}
	
	public void flush(){
		enteredRepository.flush();
	}
}
