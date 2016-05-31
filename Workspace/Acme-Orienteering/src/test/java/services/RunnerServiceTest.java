package services;

import java.util.Collection;

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
import domain.Runner;
import domain.form.ActorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class RunnerServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private ActorFormService actorFormService;
	
	// Other services needed -----------------------
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ActorService actorService;

	@Autowired
	private ClubService clubService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Registrarse en el sistema como corredor
	 */
	
	/**
	 * Positive test case: Registrarse como Runner
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Rellenar los campos
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Entrar al sistema con privilegios de administrador
	 * 		+ Comprobar que existe ese nuevo usuario entre los usuarios registrados
	 * 		+ Cerrar su sesión
	 * 		+ Comprobar que puede loguearse con el nuevo usuario creado
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewRunner() {
		// Declare variables
		ActorForm runner;
//		UserAccount userAccount;
		Actor runnerRegistered;
		Actor authenticatedRunner;
		
		// Load objects to test
		
		
		// Checks basic requirements
		
		
		// Execution of test
		runner = actorFormService.createForm(TypeOfAuthority.RUNNER);
		
		runner.setName("Nuevo");
		runner.setSurname("Runner");
		runner.setPhone("123456789");
		runner.setNif("77788999X");
		runner.setAcceptTerm(true);

//		userAccount = userAccountService.create("RUNNER");
		
		runner.setUsername("nuevoRunner");
		runner.setPassword("nuevoRunner");
		runner.setRepeatedPassword("nuevoRunner");
		
//		runner.setUserAccount(userAccount);
		
		runnerRegistered = actorFormService.saveForm(runner);
		
		// Checks results
		authenticate("admin");
		Assert.isTrue(runnerService.findAll().contains(runnerRegistered), "El runner nuevo registrado no se encuentra entre los runners registrados en el sistema."); // First check
		unauthenticate();
		
		authenticate("runner1");
		
		authenticatedRunner = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedRunner, "No se ha podido loguear al runner que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrarse como Runner con contraseña a null
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Rellenar los campos y la contraseña a null
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testNewRunnerNullPassword() {
		// Declare variables
		ActorForm runner;
//				UserAccount userAccount;
//		Actor runnerRegistered;
//		Actor authenticatedRunner;
		
		// Load objects to test
		authenticate("runner1");
		
		// Checks basic requirements
		
		
		// Execution of test
		runner = actorFormService.createForm(TypeOfAuthority.RUNNER);
		
		runner.setName("Nuevo");
		runner.setSurname("Runner");
		runner.setPhone("123456789");
		runner.setNif("77788999X");
		runner.setAcceptTerm(true);

//				userAccount = userAccountService.create("RUNNER");
		
		runner.setUsername("nuevoRunner");
		runner.setPassword(null);
		runner.setRepeatedPassword(null);
		
//				runner.setUserAccount(userAccount);
		
		actorFormService.saveForm(runner);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(runnerService.findAll().contains(runnerRegistered), "El runner nuevo registrado no se encuentra entre los runners registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("runner1");
//		
//		authenticatedRunner = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedRunner, "No se ha podido loguear al runner que se acaba de registrar."); // Second check
//		
//		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrarse como Runner estando identificado en el sistema
	 * 		- Acción
	 * 		+ Entrar el registro como runner
	 * 		+ Rellenar los campos y la contraseña a null
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testNewRunnerAsRunner() {
		// Declare variables
		ActorForm runner;
//				UserAccount userAccount;
//		Actor runnerRegistered;
//		Actor authenticatedRunner;
		
		// Load objects to test
		authenticate("runner1");
		
		// Checks basic requirements
		
		
		// Execution of test
		runner = actorFormService.createForm(TypeOfAuthority.RUNNER);
		
		runner.setName("Nuevo");
		runner.setSurname("Runner");
		runner.setPhone("123456789");
		runner.setNif("77788999X");
		runner.setAcceptTerm(true);

//				userAccount = userAccountService.create("RUNNER");
		
		runner.setUsername("nuevoRunner");
		runner.setPassword("nuevoRunner");
		runner.setRepeatedPassword("nuevoRunner");
		
//				runner.setUserAccount(userAccount);
		
		actorFormService.saveForm(runner);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(runnerService.findAll().contains(runnerRegistered), "El runner nuevo registrado no se encuentra entre los runners registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("runner1");
//		
//		authenticatedRunner = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedRunner, "No se ha podido loguear al runner que se acaba de registrar."); // Second check
//		
//		unauthenticate();

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
	 *  Positive test: Se muestran los corredores del club seleccionado.
	 */
	@Test
	public void testListRunnerByClub1(){
		
		// Declare variable
		Collection<Runner> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = runnerService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 6);

		
	}
	
	/**
	 * @see 20.a
	 *  Un usuario autenticado en el sistema debe poder:
	 *  Listar los distintos clubes y
	 *  poder ver los corredores, y toda la información sobre estos (incluyendo su currículo), que están en dicho club, 
	 *  el gerente de este, y 
	 *  en que ligas y 
	 *  carreras han participado y 
	 *  están participando.
	 *  
	 *  Positive test: Se muestran los corredores del club seleccionado.
	 */
	@Test
	public void testListRunnerByClub2(){
		
		// Declare variable
		Collection<Runner> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = runnerService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 6);
		unauthenticate();
		
	}
	
}
