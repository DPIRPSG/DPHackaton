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
	 * Rellenar la clasificación de los clubes en las carreras de las ligas que él dirige,
	 * con un procedimiento que lo haga automáticamente.
	 */
	
	/**
	 * Positive test case: Rellenar clasificación de un club.
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificación de un club
	 * 		- Comprobación
	 * 		+ Comprobar que la clasificación se ha actualizado
	 * 		+ Cerrar su sesión
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
