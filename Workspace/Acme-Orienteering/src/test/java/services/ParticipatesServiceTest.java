package services;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Manager;
import domain.Participates;
import domain.Race;
import domain.Runner;
import utilities.AbstractTest;
import utilities.InvalidPreTestException;

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
	private RunnerService runnerService;
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private RaceService raceService;
	
	
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
					.contains(runnerService.getClub(b))) { // Que su club esté inscrito en esa liga
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no está inscrito
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
					"No existe una combinación de corredor y carrera que cumpla los requisitos");			
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
	 *  Negativo: Un corredor no puede apuntarse a una carrera si ya está apuntado 
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
					.contains(runnerService.getClub(b))) { // Que su club esté inscrito en esa liga
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que está inscrito
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
					"No existe una combinación de corredor y carrera que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		participates = participatesService.joinRace(race.getId());
				
		// Checks results

		Assert.isTrue(
				participatesService.findAllClubByRunnerIdAndRaceId(runner.getId(), race.getId())
						.size() != 1, "Se ha guardado múltiples veces");

	}
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, inscribirse en carreras.
	 *  
	 *  Negativo: Inscribirse en una carrera en la que no está inscrito su club 
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
					.contains(runnerService.getClub(b))		 // Que su club NO esté inscrito en esa liga
					&& runnerService.getClub(b) != null) {	// Que tenga un club
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no está inscrito
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
					"No existe una combinación de corredor y carrera que cumpla los requisitos");			
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
					.contains(runnerService.getClub(b))) { // Que su club esté inscrito en esa liga
					contain = false;
					for (Participates e : b.getParticipates()) {
						// escanear todos para comprobar que no está inscrito
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
					"No existe una combinación de corredor y carrera que cumpla los requisitos");			
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
	 *  En caso de estar en un club , ver en cuales está inscrito.
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
					runnerService.getClub(runner))) { // Está en el mismo club que el corredor seleccionado
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
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que debería");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay más de los que debería");
	}	
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, ver la clasificación en las distintas carreas de los corredores del club.
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
					runnerService.getClub(runner))) { // Está en el mismo club que el corredor seleccionado
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
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que debería");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay más de los que debería");
	}	
	
	/**
	 * Acme-Orienteering - 21.D
	 *  En caso de estar en un club, ver la clasificación en las distintas carreas de los corredores del club.
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
					runnerService.getClub(runner))) { // Está en el mismo club que el corredor seleccionado
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
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que debería");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay más de los que debería");
	}
}
