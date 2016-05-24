package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.TypeOfAuthority;
import services.form.ActorFormService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Club;
import domain.Manager;
import domain.form.ActorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ManagerServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private ActorFormService actorFormService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ManagerService managerService;

	@Autowired
	private ClubService clubService;
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Registrar a un gerente en el sistema.
	 */
	
	/**
	 * Positive test case: Registrar un nuevo Manager
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Manager
	 * 		- Comprobación
	 * 		+ Listar los managers
	 * 		+ Comprobar que hay 1 más de los que había
	 * 		+ Comprobar que el nuevo manager se encuentra entre ellos
	 * 		+ Cerrar su sesión
	 * 		+ Comprobar que puedes loguearte con el nuevo Manager
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testRegisterManager() {
		// Declare variables
		Actor admin;
		ActorForm manager;
		Actor managerRegistered;
		Actor authenticatedManager;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		manager = actorFormService.createForm(TypeOfAuthority.MANAGER);
		
		manager.setName("Nuevo");
		manager.setSurname("Manager");
		manager.setPhone("123456789");
		manager.setNif("77788999X");
		manager.setAcceptTerm(true);

//		userAccount = userAccountService.create("MANAGER");
		
		manager.setUsername("nuevoManager");
		manager.setPassword("nuevoManager");
		manager.setRepeatedPassword("nuevoManager");
		
//		manager.setUserAccount(userAccount);
		
		managerRegistered = actorFormService.saveForm(manager);
		
		// Checks results
		authenticate("admin");
		Assert.isTrue(managerService.findAll().contains(managerRegistered), "El manager nuevo registrado no se encuentra entre los managers registrados en el sistema."); // First check
		unauthenticate();
		
		authenticate("manager1");
		
		authenticatedManager = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedManager, "No se ha podido loguear al manager que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Manager sin estar autenticado
	 * 		- Acción
	 * 		+ Registrar un nuevo Manager
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testRegisterManagerAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		ActorForm manager;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
//		
//		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		manager = actorFormService.createForm(TypeOfAuthority.MANAGER);
		
		manager.setName("Nuevo");
		manager.setSurname("Manager");
		manager.setPhone("123456789");
		manager.setNif("77788999X");
		manager.setAcceptTerm(true);

//				userAccount = userAccountService.create("MANAGER");
		
		manager.setUsername("nuevoManager");
		manager.setPassword("nuevoManager");
		manager.setRepeatedPassword("nuevoManager");
		
//				manager.setUserAccount(userAccount);
		
		actorFormService.saveForm(manager);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(managerService.findAll().contains(managerRegistered), "El manager nuevo registrado no se encuentra entre los managers registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("manager1");
//		
//		authenticatedManager = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedManager, "No se ha podido loguear al manager que se acaba de registrar."); // Second check
//		
//		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Manager con username a null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Manager con username nulo
	 * 		- Comprobación
	 * 		+ Listar los managers
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	// CORREGIR
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test 
	public void testRegisterManagerNullUsername() {
//		// Declare variables
		Actor admin;
		ActorForm manager;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		manager = actorFormService.createForm(TypeOfAuthority.MANAGER);
		
		manager.setName("Nuevo");
		manager.setSurname("Manager");
		manager.setPhone("123456789");
		manager.setNif("77788999X");
		manager.setAcceptTerm(true);

//		userAccount = userAccountService.create("MANAGER");
		
		manager.setUsername(null);
		manager.setPassword("nuevoManager");
		manager.setRepeatedPassword("nuevoManager");
		
//		manager.setUserAccount(userAccount);
		
		actorFormService.saveForm(manager);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(managerService.findAll().contains(managerRegistered), "El manager nuevo registrado no se encuentra entre los managers registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("manager1");
//		
//		authenticatedManager = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedManager, "No se ha podido loguear al manager que se acaba de registrar."); // Second check
//		
//		unauthenticate();
		
		actorService.flush();

	}
	

	/**
	 * @see 19.b
	 *  Un usuario que no haya iniciado sesión en el sistema debe poder:
	 *  Listar los distintos clubes y
	 *  poder ver los corredores, y toda la información sobre estos (incluyendo su currículo), que están en dicho club, 
	 *  el gerente de este, y 
	 *  en que ligas y 
	 *  carreras han participado y 
	 *  están participando.
	 *  
	 *  Positive test: Se muestra el manager del club seleccionado.
	 */
	@Test
	public void testListManagerByClub1(){
		
		// Declare variable
		Manager manager;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		manager = club.getManager();
		
		// Check result
		Assert.isTrue(manager.getName().equals("Guillermo"));
		
	}
	
	/**
	 * @see 20.a
	 *  Un usuario que no haya iniciado sesión en el sistema debe poder:
	 *  Listar los distintos clubes y
	 *  poder ver los corredores, y toda la información sobre estos (incluyendo su currículo), que están en dicho club, 
	 *  el gerente de este, y 
	 *  en que ligas y 
	 *  carreras han participado y 
	 *  están participando.
	 *  
	 *  Positive test: Se muestra el manager del club seleccionado.
	 */
	@Test
	public void testListManagerByClub2(){
		
		// Declare variable
		Manager manager;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		manager = club.getManager();
		
		// Check result
		Assert.isTrue(manager.getName().equals("Guillermo"));
		unauthenticate();
		
	}
	
}