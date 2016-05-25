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
					.contains(runnerService.getClub(b))) { // Que su club est� inscrito en esa liga
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no est� inscrito
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
					"No existe una combinaci�n de corredor y carrera que cumpla los requisitos");			
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
	 *  Negativo: Un corredor no puede apuntarse a una carrera si ya est� apuntado 
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
					.contains(runnerService.getClub(b))) { // Que su club est� inscrito en esa liga
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que est� inscrito
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
					"No existe una combinaci�n de corredor y carrera que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		participates = participatesService.joinRace(race.getId());
				
		// Checks results

		Assert.isTrue(
				participatesService.findAllClubByRunnerIdAndRaceId(runner.getId(), race.getId())
						.size() != 1, "Se ha guardado m�ltiples veces");

	}
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, inscribirse en carreras.
	 *  
	 *  Negativo: Inscribirse en una carrera en la que no est� inscrito su club 
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
					.contains(runnerService.getClub(b))		 // Que su club NO est� inscrito en esa liga
					&& runnerService.getClub(b) != null) {	// Que tenga un club
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no est� inscrito
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
					"No existe una combinaci�n de corredor y carrera que cumpla los requisitos");			
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
					.contains(runnerService.getClub(b))) { // Que su club est� inscrito en esa liga
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no est� inscrito
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
					"No existe una combinaci�n de corredor y carrera que cumpla los requisitos");			
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
	 *  En caso de estar en un club , ver en cuales est� inscrito.
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
					runnerService.getClub(runner))) { // Est� en el mismo club que el corredor seleccionado
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
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que deber�a");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay m�s de los que deber�a");
	}	
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, ver la clasificaci�n en las distintas carreas de los corredores del club.
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
			if (runnerService.getClub(b.getRunner()) != null && runnerService.getClub(b.getRunner()).equals(
					runnerService.getClub(runner))) { // Est� en el mismo club que el corredor seleccionado
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
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que deber�a");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay m�s de los que deber�a");
	}	
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, ver la clasificaci�n en las distintas carreas de los corredores del club.
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
			if (runnerService.getClub(b.getRunner()) != null && runnerService.getClub(b.getRunner()).equals(
					runnerService.getClub(runner))) { // Est� en el mismo club que el corredor seleccionado
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
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que deber�a");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay m�s de los que deber�a");
	}

	/**
	 * Acme-Orienteering - CORREGIR
	 * Rellenar la clasificaci�n de los corredores en las carreras de las ligas que �l dirige.
	 */
	
	/**
	 * Positive test case: Rellenar clasificaci�n de un corredor en una carrera
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaci�n de un corredor en una carrera
	 * 		- Comprobaci�n
	 * 		+ Comprobar que la clasificaci�n se ha actualizado
	 * 		+ Cerrar su sesi�n
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
			if(l.getReferee() == referee && !l.getRacing().isEmpty()){
				league = l;
				break;
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga para el referee1 para testear.");
		
		raceIterator = league.getRacing().iterator();
		
		race = raceIterator.next(); // Carrera que debe estar en la liga seleccionada
//		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera est� en la liga escogida
//			race = raceIterator.next();
//		}
		
//		Assert.isTrue(race.getLeague() == league, "No hay ninguna carrera en la liga seleccionada");
		
		participates = participatesService.findAllRefereeByRunnerIdAndRaceId(runner.getId(), race.getId()).iterator().next();
		
		participates.setResult(999);
		
		newParticipates = participatesService.saveFromClassificationEdit(participates);
		
		// Checks results
		Assert.isTrue(newParticipates.getResult() == 999, "Las clasificaciones del corredor no se han actualizado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Rellenar clasificaci�n de un corredor en una carrera cuya liga no dirige dicho �rbitro
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaci�n de un corredor en una carrera
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testUpdateParticipatesRaceOfLeagueNoOfReferee() {
		// Declare variables
		Actor referee;
//		Actor runner;
//		Race race;
		Participates participates;
//		Participates newParticipates;
//		Iterator<Race> raceIterator;
//		Collection<League> leagues;
//		League league;
//		Participates tempParticipates;
//		Iterator<Participates> participatesIterator;
//		boolean validRace;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		unauthenticate();
//		
//		authenticate("runner1");
//		runner = actorService.findByPrincipal();
//		unauthenticate();
//		
//		authenticate("referee1");
		
//		leagues = leagueService.findAll();
//		
//		league = null;
//		for(League l: leagues){
//			if(l.getReferee() != referee && !l.getRacing().isEmpty()){
//				league = l; // Liga que NO es del referee logueado y que tiene carreras.
//				break;
//			}
//		}
//		
//		Assert.notNull(league, "No hay ninguna liga que no sea del referee1 y que tenga carreras para testear.");
//				
//		raceIterator = league.getRacing().iterator();
//		
//		race = raceIterator.next();
//		
//		validRace = false;
//		
//		while(!validRace && raceIterator.hasNext()){ // Nos aseguramos de que el runner participe en alguna carrera de esa liga
//
//			participatesIterator = race.getParticipates().iterator();
//			tempParticipates = participatesIterator.next();
//			while(tempParticipates.getRunner() != runner && participatesIterator.hasNext()){ // Nos aseguramos de que el runner participa en esa carrera
//				if(tempParticipates.getRunner() == runner){
//					validRace = true;
//					break;
//				}
//				tempParticipates = participatesIterator.next();
//			}
//		
//		}
//		
//		Assert.isTrue(validRace, "No hay ninguna carrera con las caracter�sticas necesarias.");
				
//		race = raceService.findOne(139); // Cogemos una carrera de una liga que NO dirija el referee1, pero que tenga carreras en las que participe el runner 1.
		
//		participates = participatesService.findAllRefereeByRunnerIdAndRaceId(runner.getId(), race.getId()).iterator().next();
		
		participates = participatesService.findOne(169); // Cogemos la participaci�n de una carrera de una liga que NO dirija el referee1, pero que tenga carreras en las que participe el runner 1.
		
		participates.setResult(999);
		
		participatesService.saveFromClassificationEdit(participates);
		
		// Checks results
//		Assert.isTrue(newParticipates.getResult() == 999, "Las clasificaciones del corredor no se han actualizado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Rellenar clasificaci�n de un corredor en una carrera en una liga en la que no participa su Club
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaci�n de un corredor en una carrera
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: NullPointerException
	 * 		+ Cerrar su sesi�n
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
//		Collection<League> leagues;
//		League league;
//		Club club;
//		Collection<FeePayment> feePayments;
//		boolean clubJoined;
		
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
//		
//		club = clubService.findOneByRunnerId(runner.getId());
//		
//		leagues = leagueService.findAll();
//		
//		league = null;
//		for(League l: leagues){ // Cogemos una liga en la que el club del runner1 no est� apuntado
//			if(l.getReferee() == referee){
//				feePayments = l.getFeePayments();
//				clubJoined = false;
//				for(FeePayment f: feePayments){
//					if(f.getClub() == club){
//						clubJoined = true;
//						break;
//					}
//				}
//				if(!clubJoined){
//					league = l;
//					break;
//				}
//			}
//		}
		
		
//		league = leagueService.findOne(132); // Liga que dirija el referee1 en la que el club del runner1 NO participe.
		
//		Assert.notNull(league, "No hay ninguna liga que dirija el referee1 en la que el club del runner1 NO participe para testear.");
		
//		raceIterator = raceService.findAllByRunnerId(runner.getId()).iterator();
		
//		race = raceIterator.next(); // Carrera que debe estar en la liga seleccionada
//		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera est� en la liga escogida
//			race = raceIterator.next();
//		}
		
//		Assert.isTrue(race.getLeague() == league, "No hay ninguna carrera en la liga seleccionada");
		
//		participates = participatesService.findAllRefereeByRunnerIdAndRaceId(runner.getId(), race.getId()).iterator().next();
		
//		race = league.getRacing().iterator().next(); // Cogemos una carrera de la liga seleccionada
		
		race = raceService.findOne(141); // Carrera de una Liga que dirija el referee1 en la que el club del runner1 NO participe.
		
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