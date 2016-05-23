package services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ClassificationServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private ClassificationService classificationService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RunnerService runnerService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Rellenar la clasificaci�n de los clubes en las carreras de las ligas que �l dirige,
	 * con un procedimiento que lo haga autom�ticamente.
	 */
	
	/**
	 * Positive test case: Rellenar clasificaci�n de un club.
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificaci�n de un club
	 * 		- Comprobaci�n
	 * 		+ Comprobar que la clasificaci�n se ha actualizado
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testUpdateClassification() {
		// Declare variables
		Actor referee;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		
		
		// Checks results
		
		
		unauthenticate();

	}
	
}
