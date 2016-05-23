package services;

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
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Registrar a un gerente en el sistema.
	 */
	
	/**
	 * Positive test case: Registrar un nuevo Manager
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Manager
	 * 		- Comprobaci�n
	 * 		+ Listar los managers
	 * 		+ Comprobar que hay 1 m�s de los que hab�a
	 * 		+ Comprobar que el nuevo manager se encuentra entre ellos
	 * 		+ Cerrar su sesi�n
	 * 		+ Comprobar que puedes loguearte con el nuevo Manager
	 * 		+ Cerrar su sesi�n
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
	 * 		- Acci�n
	 * 		+ Registrar un nuevo Manager
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
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
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Manager con username nulo
	 * 		- Comprobaci�n
	 * 		+ Listar los managers
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
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
	
	
}
