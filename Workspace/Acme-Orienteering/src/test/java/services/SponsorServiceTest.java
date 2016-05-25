package services;

import java.util.ArrayList;
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

import utilities.AbstractTest;
import domain.Actor;
import domain.Finances;
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
	
	@Autowired
	private FinancesService financesService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * CORREGIR
	 */
	
	/**
	 * Positive test case: Listar los Sponsor
	 * 		- Acción
	 * 		+ Listar los Sponsor del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Sponsors es el esperado.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListSponsor() {
		// Declare variables
//		Actor admin;
		int sponsorsSize;
		int expectedSponsorsSize;
		
		// Load objects to test
		expectedSponsorsSize = 6; // SUSTITUIR POR EL NÚMERO ESPERADO
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		// Checks results
		Assert.isTrue(sponsorsSize == expectedSponsorsSize, "El número del Sponsors listados del sistema no es " + expectedSponsorsSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Listar los Sponsor como Admin
	 * 		- Acción
	 *  	+ Autenticarse en el sistema como Admin
	 * 		+ Listar los Sponsor del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Sponsors es el esperado.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListSponsorAsAdmin() {
		// Declare variables
		Actor admin;
		int sponsorsSize;
		int expectedSponsorsSize;
		
		// Load objects to test
		expectedSponsorsSize = 6; // SUSTITUIR POR EL NÚMERO ESPERADO
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
//		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		// Checks results
		Assert.isTrue(sponsorsSize == expectedSponsorsSize, "El número del Sponsors listados del sistema no es " + expectedSponsorsSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Listar los Sponsor como Runner
	 * 		- Acción
	 *		+ Autenticarse en el sistema como Runner
	 * 		+ Listar los Sponsor del sistema
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Sponsors es el esperado.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListSponsorAsRunner() {
		// Declare variables
		Actor runner;
		int sponsorsSize;
		int expectedSponsorsSize;
		
		// Load objects to test
		expectedSponsorsSize = 6; // SUSTITUIR POR EL NÚMERO ESPERADO
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(runner, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		// Checks results
		Assert.isTrue(sponsorsSize == expectedSponsorsSize, "El número del Sponsors listados del sistema no es " + expectedSponsorsSize);
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Crear un Sponsor
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Creaer un nuevo Sponsor
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Sponsors de ahora es el de antes más uno.
	 * 		+ Cerrar su sesión
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
		
		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un Sponsor como Manager
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Creaer un nuevo Sponsor
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
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
//		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un Sponsor con el logo nulo
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Creaer un nuevo Sponsor con el logo nulo
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
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
//		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes + 1");
		
		sponsorService.flush();
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un Sponsor con el logo nulo
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Creaer un nuevo Sponsor con el logo nulo
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
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
//		Assert.isTrue(sponsorsSize + 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes + 1");
		
		sponsorService.flush();
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Editar un Sponsor
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar un Sponsor existente
	 * 		- Comprobación
	 * 		+ Comprobar que los datos del Sponsor editado son los nuevos
	 * 		+ Cerrar su sesión
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
	 * 		- Acción
	 * 		+ Editar un Sponsor existente
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
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
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar un Sponsor existente poniendole el nombre a null
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
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
	 * Negative test case: Editar un Sponsor con descripción nula
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Editar un Sponsor existente poniendole la descripción a null
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
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
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Eliminar un Sponsor existente
	 * 		- Comprobación
	 * 		+ Comprobar que el número de Sponsor de ahora es el de antes menos uno.
	 * 		+ Comprobar que se han eliminado también las Finances de ese Sponsor.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteSponsor() {
		// Declare variables
		Actor admin;
		Sponsor sponsor;
		int sponsorsSize;
		int newSponsorsSize;
		Collection<Finances> finances;
		Collection<Finances> sponsorFinances;
		int sponsorId;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsorId = sponsor.getId();
		
		sponsorService.delete(sponsor);
		
		// Checks results
		newSponsorsSize = sponsorService.findAll().size();
		
		Assert.isTrue(sponsorsSize - 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes - 1");
		
//		financesService.flush();
//		sponsorService.flush();
		
		finances = financesService.findAll();
		
		sponsorFinances = new ArrayList<>();
		
		for(Finances f: finances){
			if(f.getSponsor().getId() == sponsorId){
				sponsorFinances.add(f);
			}
		}
		
		Assert.isTrue(sponsorFinances.isEmpty(), "No se han borrado todas las Finances del Sponsor.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un Sponsor como usuario no autenticado.
	 * 		- Acción
	 * 		+ Eliminar un Sponsor existente
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteSponsorAsAuthenticated() {
		// Declare variables
//		Actor admin;
		Sponsor sponsor;
		int sponsorsSize;
		int newSponsorsSize;
		Collection<Finances> finances;
		Collection<Finances> sponsorFinances;
		int sponsorId;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsorId = sponsor.getId();
		
		sponsorService.delete(sponsor);
		
		// Checks results
		newSponsorsSize = sponsorService.findAll().size();
		
		Assert.isTrue(sponsorsSize - 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes - 1");
		
		finances = financesService.findAll();
		
		sponsorFinances = new ArrayList<>();
		
		for(Finances f: finances){
			if(f.getSponsor().getId() == sponsorId){
				sponsorFinances.add(f);
			}
		}
		
		Assert.isTrue(sponsorFinances.isEmpty(), "No se han borrado todas las Finances del Sponsor.");
		
//		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un Sponsor como Manager.
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Manager
	 * 		+ Eliminar un Sponsor existente
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
//	@Test 
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteSponsorAsManager() {
		// Declare variables
		Actor manager;
		Sponsor sponsor;
		int sponsorsSize;
		int newSponsorsSize;
		Collection<Finances> finances;
		Collection<Finances> sponsorFinances;
		int sponsorId;
		
		// Load objects to test
		authenticate("manager1");
		manager = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(manager, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		sponsorsSize = sponsorService.findAll().size();
		
		sponsor = sponsorService.findAll().iterator().next();
		
		sponsorId = sponsor.getId();
		
		sponsorService.delete(sponsor);
		
		// Checks results
		newSponsorsSize = sponsorService.findAll().size();
		
		Assert.isTrue(sponsorsSize - 1 == newSponsorsSize, "El nuevo número de Sponsors no es el mismo de antes - 1");
		
		finances = financesService.findAll();
		
		sponsorFinances = new ArrayList<>();
		
		for(Finances f: finances){
			if(f.getSponsor().getId() == sponsorId){
				sponsorFinances.add(f);
			}
		}
		
		Assert.isTrue(sponsorFinances.isEmpty(), "No se han borrado todas las Finances del Sponsor.");
		
		unauthenticate();

	}
	
	
}
