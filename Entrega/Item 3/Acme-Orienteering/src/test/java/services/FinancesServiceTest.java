package services;

import java.util.Date;

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

import utilities.AbstractTest;
import domain.Actor;
import domain.Finances;
import domain.League;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class FinancesServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private FinancesService financesService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private SponsorService sponsorService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 6.f
	 * Gestionar los patrocinadores. Esto incluye crear, editar, borrar, listar y
	 * financiar a las ligas.
	 */
	
	/**
	 * Positive test case: Listar las Finances
	 * 		- Acción
	 * 		+ Listar las Finances del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Finances es el esperado.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListFinances() {
		// Declare variables
//		Actor admin;
		int financesSize;
		int expectedfinancesSize;
		
		// Load objects to test
		expectedfinancesSize = 7; // SUSTITUIR POR EL NÚMERO ESPERADO
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		// Checks results
		Assert.isTrue(financesSize == expectedfinancesSize, "El número del Finances listados del sistema no es " + expectedfinancesSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Listar las Finances como Manager
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Listar las Finances del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Finances es el esperado.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListFinancesAsManager() {
		// Declare variables
		Actor manager;
		int financesSize;
		int expectedfinancesSize;
		
		// Load objects to test
		expectedfinancesSize = 7; // SUSTITUIR POR EL NÚMERO ESPERADO
		authenticate("manager1");
		manager = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(manager, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		// Checks results
		Assert.isTrue(financesSize == expectedfinancesSize, "El número del Finances listados del sistema no es " + expectedfinancesSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Listar las Finances como Referee
	 * 		- Acción
	 * 	 	+ Autenticarse en el sistema como Referee
	 * 		+ Listar las Finances del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Finances es el esperado.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListFinancesAsReferee() {
		// Declare variables
		Actor referee;
		int financesSize;
		int expectedfinancesSize;
		
		// Load objects to test
		expectedfinancesSize = 7; // SUSTITUIR POR EL NÚMERO ESPERADO
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		// Checks results
		Assert.isTrue(financesSize == expectedfinancesSize, "El número del Finances listados del sistema no es " + expectedfinancesSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Crear una Finances
	 * 		- Acción
	 * 	 	+ Autenticarse en el sistema como Admin
	 * 		+ Crear una nueva Finances
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Finances de ahora es el de antes más uno.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewFinances() {
		// Declare variables
		Actor admin;
		Finances finances;
		int financesSize;
		int newFinancesSize;
		Sponsor sponsor;
		League league;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		league = leagueService.findAll().iterator().next();
		
		finances = financesService.create(sponsor.getId(), league.getId());
		
		finances.setAmount(3500.0);
		
		financesService.saveFromEdit(finances);
		
		// Checks results
		newFinancesSize = financesService.findAll().size();
		
		Assert.isTrue(financesSize + 1 == newFinancesSize, "El nuevo número de Finances no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Crear una Finances sin estar autenticado
	 * 		- Acción
	 * 		+ Crear una nueva Finances
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testNewFinancesAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		Finances finances;
//		int financesSize;
//		int newFinancesSize;
		Sponsor sponsor;
		League league;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		financesSize = financesService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		league = leagueService.findAll().iterator().next();
		
		finances = financesService.create(sponsor.getId(), league.getId());
		
		finances.setAmount(3500.0);
		
		financesService.saveFromEdit(finances);
		
		// Checks results
//		newFinancesSize = financesService.findAll().size();
//		
//		Assert.isTrue(financesSize + 1 == newFinancesSize, "El nuevo número de Finances no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	
	/**
	 * Positive test case: Crear una Finances con cantidad negativa
	 * 		- Acción
	 * 	 	+ Autenticarse en el sistema como Admin
	 * 		+ Crear una nueva Finances con cantidad negativa
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testNewFinancesLowAmount() {
		// Declare variables
		Actor admin;
		Finances finances;
//		int financesSize;
//		int newFinancesSize;
		Sponsor sponsor;
		League league;
		Finances newFinances;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		financesSize = financesService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		league = leagueService.findAll().iterator().next();
		
		finances = financesService.create(sponsor.getId(), league.getId());
		
		finances.setAmount(-0.9);
		
		newFinances = financesService.saveFromEdit(finances);
		
		// Checks results
		financesService.flush();
		
		
//		newFinancesSize = financesService.findAll().size();
//		
//		Assert.isTrue(financesSize + 1 == newFinancesSize, "El nuevo número de Finances no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Crear una Finances con cantidad con más de 2 digitos decimales
	 * 		- Acción
	 * 	 	+ Autenticarse en el sistema como Admin
	 * 		+ Crear una nueva Finances con cantidad con más de 2 digitos decimales
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
	public void testNewFinancesExcesiveDecimalDigitsAmount() {
		// Declare variables
		Actor admin;
		Finances finances;
//		int financesSize;
//		int newFinancesSize;
		Sponsor sponsor;
		League league;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		financesSize = financesService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		league = leagueService.findAll().iterator().next();
		
		finances = financesService.create(sponsor.getId(), league.getId());
		
		finances.setAmount(999999.999);
		
		financesService.saveFromEdit(finances);
		
		// Checks results
//		newFinancesSize = financesService.findAll().size();
//		
//		Assert.isTrue(financesSize + 1 == newFinancesSize, "El nuevo número de Finances no es el mismo de antes + 1");
		
		financesService.flush();
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Editar una Finances
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar una Finances existente
	 * 		- Comprobación
	 * 		+ Comprobar que los datos de la Finances editada son los nuevos
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testEditFinances() {
		// Declare variables
		Actor admin;
		Finances finances;
		Finances newFinances;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		finances = financesService.findAll().iterator().next();
		
		finances.setAmount(999);
		
		financesService.saveFromEdit(finances);
		
		// Checks results
		newFinances = financesService.findOne(finances.getId());
		
		Assert.isTrue(newFinances.getAmount() == 999, "Los datos del Finances no se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Editar una Finances con el payment moment
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar una Finances existente con su payment moment
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: 
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testEditFinancesPaymentMoment() {
		// Declare variables
		Actor admin;
		Finances finances;
		Finances newFinances;
		Date currentDate;
		Date financesDate;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		currentDate = new Date();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		finances = financesService.findAll().iterator().next();
		
		financesDate = finances.getPaymentMoment();
		
		finances.setPaymentMoment(currentDate);
		
		financesService.saveFromEdit(finances);
		
		// Checks results
		newFinances = financesService.findOne(finances.getId());
		
		Assert.isTrue(newFinances.getPaymentMoment() == currentDate, "La fecha de la Finances no es la fecha actual, es decir, no se ha editado cuando no debería.");
		Assert.isTrue(newFinances.getPaymentMoment() != financesDate, "La fecha de la Finances es la que tenía antes, es decir, no se ha editado cuando no debería.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Editar una Finances con PaymentMoment a null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar una Finances existente con payment moment a null
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
	public void testEditFinancesNullPaymentMoment() {
		// Declare variables
		Actor admin;
		Finances finances;
//		Finances newFinances;
//		Date currentDate;
//		Date financesDate;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
//		currentDate = new Date();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		finances = financesService.findAll().iterator().next();
		
//		financesDate = finances.getPaymentMoment();
		
		finances.setPaymentMoment(null);
		
		financesService.saveFromEdit(finances);
		
		// Checks results
//		newFinances = financesService.findOne(finances.getId());
//		
//		Assert.isTrue(newFinances.getPaymentMoment() != currentDate, "La fecha de la Finances es la fecha actual, es decir, se ha editado cuando no debería.");
//		Assert.isTrue(newFinances.getPaymentMoment() == financesDate, "La fecha de la Finances no es la que tenía antes, es decir, se ha editado cuando no debería.");
		
		financesService.flush();
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar una Finances
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Eliminar una Finances existente
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Finances de antes es el de ahora menos uno.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteFinances() {
		// Declare variables
		Actor admin;
		Finances finances;
		int financesSize;
		int newFinancesSize;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		finances = financesService.findAll().iterator().next();
		
		financesService.delete(finances);
		
		// Checks results
		newFinancesSize = financesService.findAll().size();
		
		Assert.isTrue(financesSize -1 == newFinancesSize, "El número del Finances listados del sistema no es el mismo de antes menos uno.");
		
		unauthenticate();


	}
	
	/**
	 * Negative test case: Eliminar una Finances como usuario no autenticado
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como no autenticado
	 * 		+ Eliminar una Finances existente
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteFinancesAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		Finances finances;
		int financesSize;
		int newFinancesSize;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		finances = financesService.findAll().iterator().next();
		
		financesService.delete(finances);
		
		// Checks results
		newFinancesSize = financesService.findAll().size();
		
		Assert.isTrue(financesSize -1 == newFinancesSize, "El número del Finances listados del sistema no es el mismo de antes menos uno.");
		
		unauthenticate();


	}
	
	/**
	 * Negative test case: Eliminar una Finances como Referee
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Eliminar una Finances existente
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteFinancesAsReferee() {
		// Declare variables
		Actor referee;
		Finances finances;
		int financesSize;
		int newFinancesSize;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		financesSize = financesService.findAll().size();
		
		finances = financesService.findAll().iterator().next();
		
		financesService.delete(finances);
		
		// Checks results
		newFinancesSize = financesService.findAll().size();
		
		Assert.isTrue(financesSize -1 == newFinancesSize, "El número del Finances listados del sistema no es el mismo de antes menos uno.");
		
		unauthenticate();


	}
	
}
