package services;


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

import domain.Club;
import domain.Entered;
import domain.Manager;
import domain.Runner;
import utilities.AbstractTest;
import utilities.InvalidPostTestException;
import utilities.InvalidPreTestException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class EnteredServiceTest extends AbstractTest {

	// Service under test -------------------------

	
	@Autowired
	private EnteredService enteredService;
	
	// Other services needed -----------------------
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private ManagerService managerService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Acme-Orienteering - 21.A
	 *  Hacer peticiones de ingreso a los distintos clubes del sistema.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testRequestClubOk() {
		// Declare variables
		Runner runner;
		Entered entered;
		Club club;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		club = null;
		try{

		for (Runner b:runnerService.findAll()){
			if(runnerService.getClub(b) == null){ // Que no tenga un club
				for (Club c:clubService.findAll()){
					boolean contain = false;
					for (Entered e:b.getEntered()){
						if (e.getClub().equals(c)){
							contain = true;
							break;
						}
					}
					if(!contain){
						club = c;
						runner = b;
						break;
					}
				}
			}
			if(club != null && runner != null){
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(club != null && runner != null,
					"No existe una combinación de corredor y club que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		entered = enteredService.create();
		entered.setClub(club);
		entered.setRunner(runner);
		entered = enteredService.save(entered);
		
		// Checks results

		Assert.isTrue(
				enteredService.findAllByRunner(runner.getId())
						.contains(entered), "No se ha guardado correctamente en entered");
		
		authenticate("admin");

		Assert.isTrue(runnerService.findOne(runner.getId()).getEntered().contains(entered),
				"No se ha guardado correctamente en runner");
		Assert.isTrue(clubService.findOne(club.getId()).getEntered().contains(entered),
				"No se ha guardado correctamente en club");
	}
	
	/**
	 * Acme-Orienteering - 21.A
	 *  Hacer peticiones de ingreso a los distintos clubes del sistema.
	 *  
	 *  Negativo: Aceptandose mientras se crea
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testRequestClubErrorMemberDirectly() {
		// Declare variables
		Runner runner;
		Entered entered;
		Club club;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		club = null;
		try{

		for (Runner b:runnerService.findAll()){
			if(runnerService.getClub(b) == null){ // Que no tenga un club
				for (Club c:clubService.findAll()){
					boolean contain = false;
					for (Entered e:b.getEntered()){
						if (e.getClub().equals(c)){
							contain = true;
							break;
						}
					}
					if(!contain){
						club = c;
						runner = b;
						break;
					}
				}
			}
			if(club != null && runner != null)
				break;
		}
		
		// Checks basic requirements
			Assert.isTrue(club != null && runner != null,
					"No existe una combinación de corredor y club que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		entered = enteredService.create();
		entered.setClub(club);
		entered.setRunner(runner);
		entered.setAcceptedMoment(new Date());
		entered.setIsMember(true);
		entered = enteredService.save(entered);
		
		// Checks results
		Assert.isTrue(!((!entered.getIsMember()) && (entered.getAcceptedMoment() == null)),
				"Se han corregido los cambios");

		try{
			//comprobar que los campos esten bien				
			Assert.isTrue(
					enteredService.findAllByRunner(runner.getId())
							.contains(entered), "No se ha guardado correctamente en entered");
			
			authenticate("admin");
			Assert.isTrue(runnerService.findOne(runner.getId()).getEntered().contains(entered),
						"No se ha guardado correctamente en runner");
			Assert.isTrue(clubService.findOne(club.getId()).getEntered().contains(entered),
						"No se ha guardado correctamente en club");
				
			
		}catch (Exception e) {
			throw new InvalidPostTestException(e.getMessage());
		}
	}
	/**
	 * Acme-Orienteering - 21.A
	 *  Hacer peticiones de ingreso a los distintos clubes del sistema.
	 *  
	 *  Negativo: Ya es miembro de un club
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testRequestClubErrorIsMemberYet() {
		// Declare variables
		Runner runner;
		Entered entered;
		Club club;
		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		club = null;
		try{
		
		for (Runner b:runnerService.findAll()){
			if(runnerService.getClub(b) != null){ // Que tenga un club
				for (Club c:clubService.findAll()){
					boolean contain = false;
					for (Entered e:b.getEntered()){
						if (e.getClub().equals(c)){
							contain = true;
							break;
						}
					}
					if(!contain){
						club = c;
						runner = b;
						break;
					}
				}
			}
			if(club != null && runner != null)
				break;
		}
		
		// Checks basic requirements
			Assert.isTrue(club != null && runner != null,
					"No existe una combinación de corredor y club que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(runner.getUserAccount().getUsername());
		
		entered = enteredService.create();
		entered.setClub(club);
		entered.setRunner(runner);
		entered = enteredService.save(entered);
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 21.B
	 *  Revisar el estado de las distintas peticiones que haya realizado.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testViewByRunner() {
		// Declare variables
		Runner runner;
		Collection<Entered> codeResult, calculateResult;

		
		// Load objects to test
		authenticate("admin");
		
		runner = null;
		calculateResult = null;
		try{
		
		for (Runner b : runnerService.findAll()) {
			if (b.getEntered().size() > 3) {
				runner = b;
				calculateResult = b.getEntered();
				break;
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
		
		codeResult = enteredService.findAllByRunner(runner.getId());
	
		// Checks results
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que debería");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay más de los que debería");
	
	}
	
	/**
	 * Acme-Orienteering - 22.c
	 *  Revisar las distintas peticiones de ingreso que le han hecho al club.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testViewByManager() {
		// Declare variables
		Manager manager;
		Collection<Entered> codeResult, calculateResult;

		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		calculateResult = null;
		try{
		
		for (Manager b : managerService.findAll()) {
			if (b.getClub() != null) {
				if (b.getClub().getEntered().size() > 3){
					manager = b;
					calculateResult = b.getClub().getEntered();
					break;
				}
			}
		}

		// Checks basic requirements
			Assert.isTrue(manager != null,
					"No existe un manager que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		codeResult = enteredService.findAllByClub(manager.getClub().getId());
	
		// Checks results
		
		Assert.isTrue(codeResult.containsAll(calculateResult), "Hay menos de los que debería");
		Assert.isTrue(calculateResult.containsAll(codeResult), "Hay más de los que debería");
	
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Aceptar peticiones de acceso.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testAcceptEnteredOk() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		try{
		
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) == null){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.accept(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());
		
		Assert.isTrue(entered.getIsMember() && entered.getAcceptedMoment() != null
				&& !entered.getIsDenied(),
				"No se han rellenado correctamente los campos");
		
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Aceptar peticiones de acceso.
	 *  
	 *  Negativo: aceptarlo en manager de otro club 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testAcceptEnteredErrorOtherManager() {
		// Declare variables
		Manager manager, otherManager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		otherManager = null;
		entered = null;
		try{
	
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) == null){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		Assert.isTrue(entered != null && manager != null,
				"No existe una combinación de manager y entered que cumpla los requisitos");			
		
		for (Manager m:managerService.findAll()){
			if(!m.equals(manager) //No es el mismo manager
					&& m.getClub() != null){ //Tiene un club asignado
				otherManager = m;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null && otherManager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(otherManager.getUserAccount().getUsername());
		
		enteredService.accept(entered);
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Aceptar peticiones de acceso.
	 *  
	 *  Negativo: Corredor con petición ya aceptada
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAcceptEnteredErrorInAClub() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null
					&& !b.getClub().equals(runnerService.getClub(b.getRunner()))){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.accept(entered);
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Aceptar peticiones de acceso.
	 *  
	 *  Negativo: Corredor con petición aceptada por ese club
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testAcceptEnteredErrorInSameClub() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null
					&& b.getClub().equals(runnerService.getClub(b.getRunner()))){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.accept(entered);
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Denegar peticiones de acceso.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testDenyEnteredOk() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) == null){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.deny(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());
		
		Assert.isTrue(!entered.getIsMember() && entered.getAcceptedMoment() == null
				&& entered.getIsDenied(),
				"No se han rellenado correctamente los campos");
		
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Denegar peticiones de acceso.
	 *  
	 *  Positivo: estando en un club
	 */
	@Test 
	public void testDenyEnteredOkInAClub() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null
					&& !b.getClub().equals(runnerService.getClub(b.getRunner()))){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.deny(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());
		
		Assert.isTrue(!entered.getIsMember() && entered.getAcceptedMoment() == null
				&& entered.getIsDenied(),
				"No se han rellenado correctamente los campos");
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Denegar peticiones de acceso.
	 *  
	 *  Negativo: manager de otro club 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDenyEnteredErrorOtherManager() {
		// Declare variables
		Manager manager, otherManager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		otherManager = null;
		try{
	
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) == null){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		Assert.isTrue(entered != null && manager != null,
				"No existe una combinación de manager y entered que cumpla los requisitos");			
		
		for (Manager m:managerService.findAll()){
			if(!m.equals(manager) //No es el mismo manager
					&& m.getClub() != null){ //Tiene un club asignado
				otherManager = m;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(otherManager.getUserAccount().getUsername());
		
		enteredService.deny(entered);
		
		// Checks results
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Denegar peticiones de acceso.
	 *  
	 *  Negativo: es una petición ya aceptada 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDenyEnteredErrorYetAccepted() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null){ // Que el usuario esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.deny(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());		
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Expulsar de un club.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testExplEnteredOk() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null
					&& b.getClub().equals(runnerService.getClub(b.getRunner()))){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.expel(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());
		
		Assert.isTrue(!entered.getIsMember() && entered.getAcceptedMoment() != null
				&& !entered.getIsDenied(),
				"No se han rellenado correctamente los campos");
		
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Expulsar de un club.
	 *  
	 *  Negativo: Hacerlo otro Manager 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testExplEnteredErrorOtherManager() {
		// Declare variables
		Manager manager, otherManager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		otherManager = null;
		
		try{

		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null
					&& b.getClub().equals(runnerService.getClub(b.getRunner()))){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		Assert.isTrue(entered != null && manager != null,
				"No existe una combinación de manager y entered que cumpla los requisitos");			
		
		for (Manager m:managerService.findAll()){
			if(!m.equals(manager) //No es el mismo manager
					&& m.getClub() != null){ //Tiene un club asignado
				otherManager = m;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(otherManager.getUserAccount().getUsername());
		
		enteredService.expel(entered);
		
		// Checks results

	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Expulsar de un club.
	 *  
	 *  Negativo: no está en ningún club 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testExplEnteredErrorNotInAnyClub() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) == null){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.expel(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());
		
	}
	/**
	 * Acme-Orienteering - 22.d
	 *  Expulsar de un club.
	 *  
	 *  Negativo: no está en ese club pero si en otro
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testExplEnteredErrorNotInThatClub() {
		// Declare variables
		Manager manager;
		Entered entered;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null
					&& !b.getClub().equals(runnerService.getClub(b.getRunner()))){ // Que el usuario no esté en ningún club
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		
		enteredService.expel(entered);
		
		// Checks results

		entered = enteredService.findOne(entered.getId());
		
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Escribir un report sobre el estado de la petición de acceso.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testReportEnteredOkAccepted() {
		// Declare variables
		Manager manager;
		Entered entered;
		String textReport;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null // Que el usuario esté en un club
					&& runnerService.getClub(b.getRunner()).equals(b.getClub()) // Que el usuario esté en el club
					&& b.getIsMember()){ // Que el usuario sea miembro del club 
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		textReport = "Este es un report de ejemplo";
		
		entered.setReport(textReport);		
		
		entered = enteredService.save(entered);
		
		// Checks results		
		
		Assert.isTrue(entered.getIsMember() && entered.getAcceptedMoment() != null
				&& !entered.getIsDenied(),
				"Se han modificado otros campos");
		
		Assert.isTrue(entered.getReport().equals(textReport), "El texto no coincide");
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Escribir un report sobre el estado de la petición de acceso.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testReportEnteredOkDenied() {
		// Declare variables
		Manager manager;
		Entered entered;
		String textReport;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null // Que el usuario esté en un club
					&& runnerService.getClub(b.getRunner()).equals(b.getClub()) // Que el usuario esté en el club
					&& !b.getIsMember() && b.getIsDenied()){ // Que el usuario sea miembro del club 
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		textReport = "Este es un report de ejemplo";
		
		entered.setReport(textReport);		
		
		entered = enteredService.save(entered);
		
		// Checks results		
		
		Assert.isTrue(!entered.getIsMember() && entered.getAcceptedMoment() == null
				&& entered.getIsDenied(),
				"Se han modificado otros campos");
		
		Assert.isTrue(entered.getReport().equals(textReport), "El texto no coincide");
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Escribir un report sobre el estado de la petición de acceso.
	 *  
	 *  Positivo: 
	 */
	@Test 
	public void testReportEnteredOkExpul() {
		// Declare variables
		Manager manager;
		Entered entered;
		String textReport;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null // Que el usuario esté en un club
					&& runnerService.getClub(b.getRunner()).equals(b.getClub()) // Que el usuario esté en el club
					&& !b.getIsMember() && !b.getIsDenied() && b.getAcceptedMoment() != null){ // Que el usuario sea miembro del club 
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		textReport = "Este es un report de ejemplo";
		
		entered.setReport(textReport);		
		
		entered = enteredService.save(entered);
		
		// Checks results		
		
		Assert.isTrue(!entered.getIsMember() && entered.getAcceptedMoment() != null
				&& ! entered.getIsDenied(),
				"Se han modificado otros campos");
		
		Assert.isTrue(entered.getReport().equals(textReport), "El texto no coincide");
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Escribir un report sobre el estado de la petición de acceso.
	 *  
	 *  Negativo: Realizarlo otro manager 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testReportEnteredErrorOtherManager() {
		// Declare variables
		Manager manager, otherManager;
		Entered entered;
		String textReport;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		otherManager = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null // Que el usuario esté en un club
					&& runnerService.getClub(b.getRunner()).equals(b.getClub()) // Que el usuario esté en el club
					&& b.getIsMember()){ // Que el usuario sea miembro del club 
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		Assert.isTrue(entered != null && manager != null,
				"No existe una combinación de manager y entered que cumpla los requisitos");			
		
		for (Manager m:managerService.findAll()){
			if(!m.equals(manager) //No es el mismo manager
					&& m.getClub() != null){ //Tiene un club asignado
				otherManager = m;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(otherManager.getUserAccount().getUsername());
		textReport = "Este es un report de ejemplo";
		
		entered.setReport(textReport);		
		
		entered = enteredService.save(entered);
		
		// Checks results		
	}
	
	/**
	 * Acme-Orienteering - 22.d
	 *  Escribir un report sobre el estado de la petición de acceso.
	 *  
	 *  Negativo: cambia si es miembro del club 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testReportEnteredError() {
		// Declare variables
		Manager manager;
		Entered entered;
		String textReport;
		
		// Load objects to test
		authenticate("admin");
		
		manager = null;
		entered = null;
		
		try{
		for (Entered b:enteredService.findAll()){
			if(runnerService.getClub(b.getRunner()) != null // Que el usuario esté en un club
					&& !b.getIsMember()){ // Que el usuario no sea miembro del club 
				manager = b.getClub().getManager();
				entered = b;
				break;
			}
		}
		
		// Checks basic requirements
			Assert.isTrue(entered != null && manager != null,
					"No existe una combinación de manager y entered que cumpla los requisitos");			
		}catch (Exception e) {
			throw new InvalidPreTestException(e.getMessage());
		}

		// Execution of test
		authenticate(manager.getUserAccount().getUsername());
		textReport = "Este es un report de ejemplo";
		
		entered.setReport(textReport);	
		entered.setIsMember(true);
		entered.setAcceptedMoment(new Date());
		
		entered = enteredService.save(entered);
		
		// Checks results		
		
		Assert.isTrue(!(entered.getIsMember() || entered.getAcceptedMoment() != null),
				"Se han modificado campos que no deberían");
	}
}
