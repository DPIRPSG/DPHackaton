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
import domain.League;
import domain.Referee;
import domain.form.ActorForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class RefereeServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private ActorFormService actorFormService;
	
	// Other services needed -----------------------
	
	@Autowired
	private RefereeService refereeService;
	
	@Autowired
	private ActorService actorService;

	@Autowired
	private LeagueService leagueService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Registrar a un gerente en el sistema.
	 */
	
	/**
	 * Positive test case: Registrar un nuevo Referee
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Referee
	 * 		- Comprobaci�n
	 * 		+ Listar los referees
	 * 		+ Comprobar que hay 1 m�s de los que hab�a
	 * 		+ Comprobar que el nuevo referee se encuentra entre ellos
	 * 		+ Cerrar su sesi�n
	 * 		+ Comprobar que puedes loguearte con el nuevo Referee
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testRegisterReferee() {
		// Declare variables
		Actor admin;
		ActorForm referee;
		Actor refereeRegistered;
		Actor authenticatedReferee;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		referee = actorFormService.createForm(TypeOfAuthority.REFEREE);
		
		referee.setName("Nuevo");
		referee.setSurname("Referee");
		referee.setPhone("123456789");
		referee.setNif("77788999X");
		referee.setAcceptTerm(true);

//		userAccount = userAccountService.create("REFEREE");
		
		referee.setUsername("nuevoReferee");
		referee.setPassword("nuevoReferee");
		referee.setRepeatedPassword("nuevoReferee");
		
//		referee.setUserAccount(userAccount);
		
		refereeRegistered = actorFormService.saveForm(referee);
		
		// Checks results
		authenticate("admin");
		Assert.isTrue(refereeService.findAll().contains(refereeRegistered), "El referee nuevo registrado no se encuentra entre los referees registrados en el sistema."); // First check
		unauthenticate();
		
		authenticate("referee1");
		
		authenticatedReferee = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedReferee, "No se ha podido loguear al referee que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Referee sin estar autenticado
	 * 		- Acci�n
	 * 		+ Registrar un nuevo Referee
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testRegisterRefereeAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		ActorForm referee;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
//		
//		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		referee = actorFormService.createForm(TypeOfAuthority.REFEREE);
		
		referee.setName("Nuevo");
		referee.setSurname("Referee");
		referee.setPhone("123456789");
		referee.setNif("77788999X");
		referee.setAcceptTerm(true);

//				userAccount = userAccountService.create("REFEREE");
		
		referee.setUsername("nuevoReferee");
		referee.setPassword("nuevoReferee");
		referee.setRepeatedPassword("nuevoReferee");
		
//				referee.setUserAccount(userAccount);
		
		actorFormService.saveForm(referee);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(refereeService.findAll().contains(refereeRegistered), "El referee nuevo registrado no se encuentra entre los referees registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("referee1");
//		
//		authenticatedReferee = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedReferee, "No se ha podido loguear al referee que se acaba de registrar."); // Second check
//		
//		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Referee con NIF a null
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Referee con el NIF nulo
	 * 		- Comprobaci�n
	 * 		+ Listar los referees
	 * 		+ Comprobar que salta una excepci�n del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test 
	public void testRegisterRefereeNullNIF() {
//		// Declare variables
		Actor admin;
		ActorForm referee;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		referee = actorFormService.createForm(TypeOfAuthority.REFEREE);
		
		referee.setName("Nuevo");
		referee.setSurname("Referee");
		referee.setPhone("123456789");
		referee.setNif(null);
		referee.setAcceptTerm(true);

//		userAccount = userAccountService.create("REFEREE");
		
		referee.setUsername("nuevoReferee");
		referee.setPassword("nuevoReferee");
		referee.setRepeatedPassword("nuevoReferee");
		
//		referee.setUserAccount(userAccount);
		
		actorFormService.saveForm(referee);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(refereeService.findAll().contains(refereeRegistered), "El referee nuevo registrado no se encuentra entre los referees registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("referee1");
//		
//		authenticatedReferee = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedReferee, "No se ha podido loguear al referee que se acaba de registrar."); // Second check
//		
//		unauthenticate();
		
		actorService.flush();

	}
	
	/**
	 * @see 1.C
	 *  Un usuario que no haya iniciado sesi�n en el sistema debe poder:
	 *  Listar las distintas ligas, 
	 *  ver la clasificaci�n, 
	 *  las carreras que la componen, 
	 *  los clubes que participan en ella y 
	 *  el �rbitro que la dirige.
	 *  
	 *  Positive test: Se muestran el �rbitro de la liga coleccionada.
	 */
	@Test
	public void testListRefereeByLeague1(){
		
		// Declare variable
		Referee result;
		Collection<League> allLeagues;
		League league;
		
		// Load object to test
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = league.getReferee();
		
		// Check result
		Assert.isTrue(result.getName().equals("Carlos"));
		
	}
	
	/**
	 * @see 2.A
	 *  Un usuario autenticado en el sistema debe poder:
	 *  Listar las distintas ligas, 
	 *  ver la clasificaci�n, 
	 *  las carreras que la componen, 
	 *  los clubes que participan en ella y 
	 *  el �rbitro que la dirige.
	 *  
	 *  Positive test: Se muestran el �rbitro de la liga coleccionada.
	 */
	@Test
	public void testListRefereeByLeague2(){
		
		// Declare variable
		Referee result;
		Collection<League> allLeagues;
		League league;
		
		// Load object to test
		authenticate("runner1");
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = league.getReferee();
		
		// Check result
		Assert.isTrue(result.getName().equals("Carlos"));
		unauthenticate();
		
	}
	
}
