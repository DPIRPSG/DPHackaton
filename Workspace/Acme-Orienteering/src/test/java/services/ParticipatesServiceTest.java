package services;

import java.util.Collection;
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
import domain.Actor;
import domain.Club;
import domain.FeePayment;
import domain.League;
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
	 * Acme-Orienteering - CORREGIR
	 * Rellenar la clasificación de los corredores en las carreras de las ligas que él dirige.
	 */
	
	/**
	 * Positive test case: Rellenar clasificación de un corredor en una carrera
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificación de un corredor en una carrera
	 * 		- Comprobación
	 * 		+ Comprobar que la clasificación se ha actualizado
	 * 		+ Cerrar su sesión
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
		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera esté en la liga escogida
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
	 * Negative test case: Rellenar clasificación de un corredor en una carrera
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificación de un corredor en una carrera
	 * 		- Comprobación
	 * 		+ Comprobar que la clasificación se ha actualizado
	 * 		+ Cerrar su sesión
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
		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera esté en la liga escogida
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
	 * Negative test case: Rellenar clasificación de un corredor en una carrera
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificación de un corredor en una carrera
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: NullPointerException
	 * 		+ Cerrar su sesión
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
		for(League l: leagues){ // Cogemos una liga en la que el club del runner1 no esté apuntado
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
//		while(race.getLeague() == league && raceIterator.hasNext()){ // Nos aseguramos de que la carrera esté en la liga escogida
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
