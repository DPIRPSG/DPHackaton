package services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import utilities.InvalidPreTestException;
import domain.Actor;
import domain.Club;
import domain.FeePayment;
import domain.League;
import domain.Manager;
import domain.Participates;
import domain.Race;
import domain.Runner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ParticipatesServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private ParticipatesService participatesService;

	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private RaceService raceService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ClubService clubService;
	
	
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, inscribirse en carreras.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testJoinRaceOk() {
		// Declare variables
		Runner runner;
		Race race;
		Participates participates;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		race = null;
		try{
		
		for (Runner b : runnerService.findAll()) {
			for (Race c : raceService.findAll()) {
				boolean contain = true;
				if (c.getMoment().after(new Date()) // Que no haya pasado
					&& clubService.findAllByLeagueId(c.getLeague().getId())
					.contains(runnerService.getClub(b))) { // Que su club estï¿½ inscrito en esa liga
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no estï¿½ inscrito
						if (e.getRace().equals(c)) {
							contain = true;
							break;
						}
					}
				}
				if (!contain) {
					race = c;
					runner = b;
					break;
				}
			}
			if(race != null && runner != null){
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(race != null && runner != null,
					"No existe una combinaciï¿½n de corredor y carrera que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		participates = participatesService.joinRace(race.getId());
				
		// Checks results

		Assert.isTrue(
				participatesService.findAllClubByRunnerIdAndRaceId(runner.getId(), race.getId())
						.contains(participates), "No se ha guardado correctamente en entered");
		
		authenticate("admin");

		Assert.isTrue(runnerService.findOne(runner.getId()).getParticipates().contains(participates),
				"No se ha guardado correctamente en runner");
		Assert.isTrue(raceService.findOne(race.getId()).getParticipates().contains(participates),
				"No se ha guardado correctamente en race");
	}
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, inscribirse en carreras.
	 *  
	 *  Negativo: Un corredor no puede apuntarse a una carrera si ya estï¿½ apuntado 
	 */
	@Test 
	public void testJoinRaceErrorMultipleJoin() {
		// Declare variables
		Runner runner;
		Race race;
		Participates participates;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		race = null;
		try{
		
		for (Runner b : runnerService.findAll()) {
			for (Race c : raceService.findAll()) {
				boolean contain = false;
				if (c.getMoment().after(new Date()) // Que no haya pasado
					&& clubService.findAllByLeagueId(c.getLeague().getId())
					.contains(runnerService.getClub(b))) { // Que su club estï¿½ inscrito en esa liga
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que estï¿½ inscrito
						if (e.getRace().equals(c)) {
							contain = true;
							break;
						}
					}
				}
				if (contain) {
					race = c;
					runner = b;
					break;
				}
			}
			if(race != null && runner != null){
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(race != null && runner != null,
					"No existe una combinaciï¿½n de corredor y carrera que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		participates = participatesService.joinRace(race.getId());
				
		// Checks results

		Assert.isTrue(
				participatesService.findAllClubByRunnerIdAndRaceId(runner.getId(), race.getId())
						.size() != 1, "Se ha guardado mï¿½ltiples veces");

	}
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, inscribirse en carreras.
	 *  
	 *  Negativo: Inscribirse en una carrera en la que no estï¿½ inscrito su club 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testJoinRaceErrorClubNotInLeague() {
		// Declare variables
		Runner runner;
		Race race;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		race = null;
		
		try{

		for (Runner b : runnerService.findAll()) {
			for (Race c : raceService.findAll()) {
				boolean contain = true;
				if (c.getMoment().after(new Date()) // Que no haya pasado
					&& ! clubService.findAllByLeagueId(c.getLeague().getId())
					.contains(runnerService.getClub(b))		 // Que su club NO estï¿½ inscrito en esa liga
					&& runnerService.getClub(b) != null) {	// Que tenga un club
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no estï¿½ inscrito
						if (e.getRace().equals(c)) {
							contain = true;
							break;
						}
					}
				}
				if (!contain) {
					race = c;
					runner = b;
					break;
				}

			}
			if(race != null && runner != null){
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(race != null && runner != null,
					"No existe una combinaciï¿½n de corredor y carrera que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		participatesService.joinRace(race.getId());
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, inscribirse en carreras.
	 *  
	 *  Negativo: Inscribirse en una carrera que ya ha pasado 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testJoinRaceErrorInPast() {
		// Declare variables
		Runner runner;
		Race race;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		race = null;
		
		try{

		for (Runner b : runnerService.findAll()) {
			for (Race c : raceService.findAll()) {
				boolean contain = true;
				if (!c.getMoment().after(new Date()) // Que no haya pasado
					&& clubService.findAllByLeagueId(c.getLeague().getId())
					.contains(runnerService.getClub(b))) { // Que su club estï¿½ inscrito en esa liga
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no estï¿½ inscrito
						if (e.getRace().equals(c)) {
							contain = true;
							break;
						}
					}
				}
				if (!contain) {
					race = c;
					runner = b;
					break;
				}

			}
			if(race != null && runner != null){
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(race != null && runner != null,
					"No existe una combinaciï¿½n de corredor y carrera que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		participatesService.joinRace(race.getId());
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club , ver en cuales estï¿½ inscrito.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testViewJoinsByRunner() {
		// Declare variables
		Runner runner;
		Collection<Participates> codeResult, calculateResult;

		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		calculateResult = null;
		try{
		
		for (Participates b : participatesService.findAll()) {
			if (b.getRace().getMoment().after(new Date()) // La carrera no ha pasado
					&& b.getRace().getParticipates().size() > 1) { //Hay al menos 2 inscritos 
				runner = b.getRunner();
				break;
			}
		}
		
		Assert.isTrue(runner != null,
				"No existe un corredor que cumpla los requisitos");	
		
		calculateResult = new ArrayList<Participates>();
		for (Participates b : participatesService.findAll()){
			if (runnerService.getClub(b.getRunner()).equals(
					runnerService.getClub(runner))) { // Estï¿½ en el mismo club que el corredor seleccionado
				calculateResult.add(b);
			}
		}

		// Checks basic requirements
			Assert.isTrue(runner != null,
					"No existe un corredor que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		codeResult = participatesService.findAllClubByRunnerIdAndRaceId(-1, -1);
	
		// Checks results
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que deberï¿½a");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay mï¿½s de los que deberï¿½a");
	}	
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, ver la clasificaciï¿½n en las distintas carreas de los corredores del club.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testViewResultByRunner() {
		// Declare variables
		Runner runner;
		Collection<Participates> codeResult, calculateResult;

		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		calculateResult = null;
		try{
		
		for (Participates b : participatesService.findAll()) {
			if (b.getRace().getMoment().before(new Date()) // La carrera no ha pasado
					&& b.getRace().getParticipates().size() > 1) { //Hay al menos 2 inscritos 
				runner = b.getRunner();
				break;
			}
		}
		
		Assert.isTrue(runner != null,
				"No existe un corredor que cumpla los requisitos");	
		
		calculateResult = new ArrayList<Participates>();
		for (Participates b : participatesService.findAll()){
			if (runnerService.getClub(b.getRunner()).equals(
					runnerService.getClub(runner))) { // Estï¿½ en el mismo club que el corredor seleccionado
				calculateResult.add(b);
			}
		}

		// Checks basic requirements
			Assert.isTrue(runner != null,
					"No existe un corredor que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		codeResult = participatesService.findAllClubByRunnerIdAndRaceId(-1, -1);
	
		// Checks results
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que deberï¿½a");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay mï¿½s de los que deberï¿½a");
	}	
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, ver la clasificaciï¿½n en las distintas carreas de los corredores del club.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testViewResultByManager() {
		// Declare variables
		Runner runner;
		Manager manager;
		Collection<Participates> codeResult, calculateResult;

		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		calculateResult = null;
		manager = null;
		try{
		
		for (Participates b : participatesService.findAll()) {
			if (b.getRace().getMoment().before(new Date()) // La carrera no ha pasado
					&& b.getRace().getParticipates().size() > 1) { //Hay al menos 2 inscritos 
				runner = b.getRunner();
				manager = runnerService.getClub(b.getRunner()).getManager();
				break;
			}
		}
		
		Assert.isTrue(runner != null,
				"No existe un corredor que cumpla los requisitos");	
		
		calculateResult = new ArrayList<Participates>();
		for (Participates b : participatesService.findAll()){
			if (runnerService.getClub(b.getRunner()).equals(
					runnerService.getClub(runner))) { // Estï¿½ en el mismo club que el corredor seleccionado
				calculateResult.add(b);
			}
		}

		// Checks basic requirements
			Assert.isTrue(runner != null && manager != null,
					"No existe un corredor, manager que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		codeResult = participatesService.findAllClubByRunnerIdAndRaceId(-1, -1);
	
		// Checks results
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que deberï¿½a");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay mï¿½s de los que deberï¿½a");
	}

	/**
	 * Acme-Orienteering - CORREGIR
	 * Rellenar la clasificaciï¿½n de los corredores en las carreras de las ligas que ï¿½l dirige.
	 */
	
	/**
	 * Positive test case: Rellenar clasificaciï¿½n de un corredor en una carrera
	 * 		- Acciï¿½n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaciï¿½n de un corredor en una carrera
	 * 		- Comprobaciï¿½n
	 * 		+ Comprobar que la clasificaciï¿½n se ha actualizado
	 * 		+ Cerrar su sesiï¿½n
	 */
	
	@Test 
	public void testUpdateParticipates() {
		// Declare variables
		Actor referee;
		Actor runner;
		Race race;
		Participates participates;
		Participates newParticipates;
		Iterator<Race> raceIterator;
		Collection<League> leagues;
		League league;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		unauthenticate();
		
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		unauthenticate();
		
		authenticate("referee1");
		
		leagues = leagueService.findAll();
		
		league = null;
		for(League l: leagues){
			if(l.getReferee() == referee){
				league = l;
				break;
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga para el referee1 para testear.");
		
		raceIterator = raceService.findAllByRunnerId(runner.getId()).iterator();
		
		race = raceIterator.next(); // Carrera que debe estar en la liga seleccionada
		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera estï¿½ en la liga escogida
			race = raceIterator.next();
		}
		
		Assert.isTrue(race.getLeague() == league, "No hay ninguna carrera en la liga seleccionada");
		
		participates = participatesService.findAllRefereeByRunnerIdAndRaceId(runner.getId(), race.getId()).iterator().next();
		
		participates.setResult(999);
		
		newParticipates = participatesService.saveFromClassificationEdit(participates);
		
		// Checks results
		Assert.isTrue(newParticipates.getResult() == 999, "Las clasificaciones del corredor no se han actualizado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Rellenar clasificaciï¿½n de un corredor en una carrera
	 * 		- Acciï¿½n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaciï¿½n de un corredor en una carrera
	 * 		- Comprobaciï¿½n
	 * 		+ Comprobar que la clasificaciï¿½n se ha actualizado
	 * 		+ Cerrar su sesiï¿½n
	 */
	
	// CORREGIR
	@Test 
	public void testUpdateParticipatesRaceOfLeagueNoOfReferee() {
		// Declare variables
		Actor referee;
		Actor runner;
		Race race;
		Participates participates;
		Participates newParticipates;
		Iterator<Race> raceIterator;
		Collection<League> leagues;
		League league;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		unauthenticate();
		
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		unauthenticate();
		
		authenticate("referee1");
		
		leagues = leagueService.findAll();
		
		league = null;
		for(League l: leagues){
			if(l.getReferee() != referee){
				league = l; // Liga que NO es del referee logueado
				break;
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga que no sea del referee1 para testear.");
		
		raceIterator = raceService.findAllByRunnerId(runner.getId()).iterator();
		
		race = raceIterator.next(); // Carrera que debe estar en la liga seleccionada
		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera está en la liga escogida
			race = raceIterator.next();
		}
		
		Assert.isTrue(race.getLeague() == league, "No hay ninguna carrera en la liga seleccionada");
		
		participates = participatesService.findAllRefereeByRunnerIdAndRaceId(runner.getId(), race.getId()).iterator().next();
		
		participates.setResult(999);
		
		newParticipates = participatesService.saveFromClassificationEdit(participates);
		
		// Checks results
		Assert.isTrue(newParticipates.getResult() == 999, "Las clasificaciones del corredor no se han actualizado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Rellenar clasificaciï¿½n de un corredor en una carrera
	 * 		- Acciï¿½n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaciï¿½n de un corredor en una carrera
	 * 		- Comprobaciï¿½n
	 * 		+ Comprobar que salta una excepciï¿½n del tipo: NullPointerException
	 * 		+ Cerrar su sesiï¿½n
	 */
	
//	@Test 
	@Test(expected=NullPointerException.class)
	@Rollback(value = true)
	public void testUpdateParticipatesRunnerOfClubThatDontParticipatesOnLeague() {
		// Declare variables
		Actor referee;
		Runner runner;
		Race race;
		Participates participates;
		Participates newParticipates;
//		Iterator<Race> raceIterator;
		Collection<League> leagues;
		League league;
		Club club;
		Collection<FeePayment> feePayments;
		boolean clubJoined;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		unauthenticate();
		
		authenticate("runner1");
		runner = runnerService.findByPrincipal();
		unauthenticate();
		
		authenticate("referee1");
		
		club = clubService.findOneByRunnerId(runner.getId());
		
		leagues = leagueService.findAll();
		
		league = null;
		for(League l: leagues){ // Cogemos una liga en la que el club del runner1 no estï¿½ apuntado
			if(l.getReferee() == referee){
				feePayments = l.getFeePayments();
				clubJoined = false;
				for(FeePayment f: feePayments){
					if(f.getClub() == club){
						clubJoined = true;
						break;
					}
				}
				if(!clubJoined){
					league = l;
					break;
				}
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga que dirija el referee1 en la que el club del runner1 NO participe para testear.");
		
//		raceIterator = raceService.findAllByRunnerId(runner.getId()).iterator();
		
//		race = raceIterator.next(); // Carrera que debe estar en la liga seleccionada
//		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera estï¿½ en la liga escogida
//			race = raceIterator.next();
//		}
		
//		Assert.isTrue(race.getLeague() == league, "No hay ninguna carrera en la liga seleccionada");
		
//		participates = participatesService.findAllRefereeByRunnerIdAndRaceId(runner.getId(), race.getId()).iterator().next();
		
		race = league.getRacing().iterator().next(); // Cogemos una carrera de la liga seleccionada
		
		participates = participatesService.create();
		
		participates.setResult(500);
		participates.setRunner(runner);
		participates.setRace(race);
		
		newParticipates = participatesService.saveFromClassificationEdit(participates);
		
		// Checks results
		Assert.isTrue(newParticipates.getResult() == 500, "Las clasificaciones del corredor se han creado correctamente.");
		
		unauthenticate();

	}

}