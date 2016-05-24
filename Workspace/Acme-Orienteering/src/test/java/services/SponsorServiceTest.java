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

import utilities.AbstractTest;
import domain.Actor;
import domain.Sponsor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class SponsorServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private SponsorService sponsorService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * CORREGIR
	 */
	
	/**
	 * Positive test case: Listar los Sponsor
	 * 		- Acci�n
	 * 		+ Listar los Sponsor del sistema
	 * 		- Comprobaci�n
	 * 		+ Comprobar que el n�mero de Sponsors es el esperado.
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testListSponsor() {
		// Declare variables
//		Actor admin;
		int sponsorsSize;
		int expectedSponsorsSize;
		
		// Load objects to test
		expectedSponsorsSize = 1; // SUSTITUIR POR EL N�MERO ESPERADO
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		// Checks results
		Assert.isTrue(sponsorsSize == expectedSponsorsSize, "El n�mero del Sponsors listados del sistema no es " + expectedSponsorsSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Listar los Sponsor como Admin
	 * 		- Acci�n
	 *  	+ Autenticarse en el sistema como Admin
	 * 		+ Listar los Sponsor del sistema
	 * 		- Comprobaci�n
	 * 		+ Comprobar que el n�mero de Sponsors es el esperado.
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testListSponsorAsAdmin() {
		// Declare variables
		Actor admin;
		int sponsorsSize;
		int expectedSponsorsSize;
		
		// Load objects to test
		expectedSponsorsSize = 1; // SUSTITUIR POR EL N�MERO ESPERADO
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
//		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		// Checks results
		Assert.isTrue(sponsorsSize == expectedSponsorsSize, "El n�mero del Sponsors listados del sistema no es " + expectedSponsorsSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Listar los Sponsor como Runner
	 * 		- Acci�n
	 *		+ Autenticarse en el sistema como Runner
	 * 		+ Listar los Sponsor del sistema
	 * 		- Comprobaci�n
	 * 		+ Comprobar que el n�mero de Sponsors es el esperado.
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testListSponsorAsRunner() {
		// Declare variables
		Actor runner;
		int sponsorsSize;
		int expectedSponsorsSize;
		
		// Load objects to test
		expectedSponsorsSize = 1; // SUSTITUIR POR EL N�MERO ESPERADO
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(runner, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		// Checks results
		Assert.isTrue(sponsorsSize == expectedSponsorsSize, "El n�mero del Sponsors listados del sistema no es " + expectedSponsorsSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Crear un Sponsor
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Creaer un nuevo Sponsor
	 * 		- Comprobaci�n
	 * 		+ Comprobar que el n�mero de Sponsors de ahora es el de antes m�s uno.
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testNewSponsor() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
		int sponsorsSize;
		int newSponsorsSize;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.create();
		
		sponsor.setDescription("Nuevo Sponsor");
		sponsor.setLogo("http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg");
		sponsor.setName("Nuevo Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
		newSponsorsSize = sponsorService.findAll().size();
		
		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo n�mero de Sponsors no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un Sponsor como Manager
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Creaer un nuevo Sponsor
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testNewSponsorByManager() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
//		int sponsorsSize;
//		int newSponsorsSize;
		
		// Load objects to test
		authenticate("manager1");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.create();
		
		sponsor.setDescription("Nuevo Sponsor");
		sponsor.setLogo("http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg");
		sponsor.setName("Nuevo Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
//		newSponsorsSize = sponsorService.findAll().size();
//		
//		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo n�mero de Sponsors no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un Sponsor con el logo nulo
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Creaer un nuevo Sponsor con el logo nulo
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test 
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
	public void testNewSponsorNullLogo() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
//		int sponsorsSize;
//		int newSponsorsSize;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.create();
		
		sponsor.setDescription("Nuevo Sponsor");
		sponsor.setLogo(null);
		sponsor.setName("Nuevo Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
//		newSponsorsSize = sponsorService.findAll().size();
//		
//		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo n�mero de Sponsors no es el mismo de antes + 1");
		
		sponsorService.flush();
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un Sponsor con el logo nulo
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Creaer un nuevo Sponsor con el logo nulo
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test 
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
	public void testNewSponsorBlankLogo() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
//		int sponsorsSize;
//		int newSponsorsSize;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.create();
		
		sponsor.setDescription("Nuevo Sponsor");
		sponsor.setLogo("");
		sponsor.setName("Nuevo Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
//		newSponsorsSize = sponsorService.findAll().size();
//		
//		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo n�mero de Sponsors no es el mismo de antes + 1");
		
		sponsorService.flush();
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Editar un Sponsor
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar un Sponsor existente
	 * 		- Comprobaci�n
	 * 		+ Comprobar que los datos del Sponsor editado son los nuevos
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testEditSponsor() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
		Sponsor newSponsor;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsor.setDescription("Edited Sponsor");
		sponsor.setLogo("http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg");
		sponsor.setName("Edited Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
		newSponsor = sponsorService.findOne(sponsor.getId());
		
		Assert.isTrue(newSponsor.getName() == "Edited Sponsor" && newSponsor.getDescription() == "Edited Sponsor" && newSponsor.getLogo() == "http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg", "Los datos del Sponsor no se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Editar un Sponsor sin autenticarse
	 * 		- Acci�n
	 * 		+ Editar un Sponsor existente
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditSponsorAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		Sponsor sponsor;
		Sponsor newSponsor;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsor.setDescription("Edited Sponsor");
		sponsor.setLogo("http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg");
		sponsor.setName("Edited Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
		newSponsor = sponsorService.findOne(sponsor.getId());
		
		Assert.isTrue(newSponsor.getName() == "Edited Sponsor" && newSponsor.getDescription() == "Edited Sponsor" && newSponsor.getLogo() == "http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg", "Los datos del Sponsor no se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Editar un Sponsor con nombre nulo
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar un Sponsor existente poniendole el nombre a null
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditSponsorNullName() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
		Sponsor newSponsor;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsor.setDescription("Edited Sponsor");
		sponsor.setLogo("http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg");
		sponsor.setName(null);
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
		newSponsor = sponsorService.findOne(sponsor.getId());
		
		Assert.isTrue(newSponsor.getName() == "Edited Sponsor" && newSponsor.getDescription() == "Edited Sponsor" && newSponsor.getLogo() == "http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg", "Los datos del Sponsor no se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Editar un Sponsor con descripci�n nula
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar un Sponsor existente poniendole la descripci�n a null
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta una excepci�n del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditSponsorNullDescription() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
		Sponsor newSponsor;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsor.setDescription(null);
		sponsor.setLogo("http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg");
		sponsor.setName("Edited Sponsor");
		
		sponsorService.saveFromEdit(sponsor);
		
		// Checks results
		newSponsor = sponsorService.findOne(sponsor.getId());
		
		Assert.isTrue(newSponsor.getName() == "Edited Sponsor" && newSponsor.getDescription() == "Edited Sponsor" && newSponsor.getLogo() == "http://thumb101.shutterstock.com/display_pic_with_logo/481717/215348707/stock-vector-sponsor-stamp-215348707.jpg", "Los datos del Sponsor no se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar un Sponsor
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Eliminar un Sponsor existente
	 * 		- Comprobaci�n
	 * 		+ Comprobar que el n�mero de Sponsor de ahora es el de antes menos uno.
	 * 		+ Cerrar su sesi�n
	 */
	
	// CORREGIR
	@Test 
	@Rollback(value = true)
	public void testDeleteSponsor() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
		int sponsorsSize;
		int newSponsorsSize;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsorService.delete(sponsor);
		
		// Checks results
		newSponsorsSize = sponsorService.findAll().size();
		
		Assert.isTrue(sponsorsSize - 1 == newSponsorsSize, "El nuevo n�mero de Sponsors no es el mismo de antes - 1");
		
		unauthenticate();

	}
	
	// CORREGIR : Falta primer test negativo de Delete Sponsor
	
	// CORREGIR : Falta segundo test negativo de Delete Sponsor
	
	
}
