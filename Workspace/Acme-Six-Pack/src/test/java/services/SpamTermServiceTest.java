package services;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.SpamTerm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class SpamTermServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private SpamTermService spamTermService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Six-Pack - Level B - 18.1
	 * Change the list of terms that the system uses to flag a message as spam.
	 */
	
	/**
	 * Positive test case: Listar todos
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		- Comprobaci�n
	 * 		+ Comprobar que son 10 t�rminos (los que deben estar por defecto en el sistema)
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testListSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(!spamTerms.isEmpty() && spamTerms.size() == 10, "No se han obtenido los 10 t�rminos de spam esperados");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: A�adir uno nuevo
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ A�adir el t�rmino "newSpamTerm"
	 * 		- Comprobaci�n
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Comprobar que son 11 t�rminos
	 * 		+ Comprobar que uno de ellos es "newSpamTerm"
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testAddSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		SpamTerm spamTermAdded;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm("newSpamTerm");
		
		spamTermAdded = spamTermService.save(spamTermToAdd);
		
		spamTerms = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(spamTerms.size() == 11, "No hay los 11 spamTerms que se esperaban en el sistema tras a�adir uno nuevo.");
		Assert.isTrue(spamTerms.contains(spamTermAdded), "No existe el nuevo t�rmino a�adido en la lista de t�rminos de spam del sistema.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Editar una existente
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Editar un t�rmino de la lista por "editedSpamTerm"
	 * 		- Comprobaci�n
	 * 		+ Comprobar que siguen siendo 10 los t�rminos del sistema
	 * 		+ Comprobar que no existe el t�rmino que ha sido editado
	 * 		+ Comrobar que "editedSpamTerm" se encuentra entre los t�rminos del sistema
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testEditSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToEdit;
		SpamTerm spamTermEdited;
		String originalSpamTermName;
		Collection<SpamTerm> spamTermsEdited;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		spamTermToEdit = spamTerms.iterator().next();
		
		originalSpamTermName = spamTermToEdit.getTerm();
		
		spamTermToEdit.setTerm("editedSpamTerm");
		spamTermEdited = spamTermService.save(spamTermToEdit);
		
		spamTermsEdited = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(!spamTerms.isEmpty() && spamTerms.size() == 10, "No se han obtenido los 10 t�rminos de spam esperados tras la edici�n.");
		for(SpamTerm sp: spamTermsEdited){
			if(sp.getTerm().equals(originalSpamTermName)){
				Assert.isTrue(false, "El t�rmino de spam antes de ser editado sigue en la lista de t�rminos del sistema.");
			}
		}
		Assert.isTrue(spamTermsEdited.contains(spamTermEdited), "El t�rmino de spam editado no aparece en la lista de t�rminos del sistema.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar uno existente
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Eliminar uno existente
	 * 		- Comprobaci�n
	 * 		+ Comprobar que ahora hay 9 t�rminos en el sistema
	 * 		+ Comprobar que el t�rmino que hemos eliminado no est� entre esos 9 obtenidos
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test 
	public void testDeleteSpamTerm() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToDelete;
		String originalSpamTermName;
		Collection<SpamTerm> spamTermsEdited;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		spamTermToDelete = spamTerms.iterator().next();
		
		originalSpamTermName = spamTermToDelete.getTerm();
		
		spamTermService.delete(spamTermToDelete);
		
		spamTermsEdited = spamTermService.findAll();
		
		// Checks results
		Assert.isTrue(!spamTermsEdited.isEmpty() && spamTermsEdited.size() == 9, "No se han obtenido los 9 t�rminos de spam esperados tras la edici�n.");
		for(SpamTerm sp: spamTermsEdited){
			if(sp.getTerm().equals(originalSpamTermName)){
				Assert.isTrue(false, "El t�rmino de spam borrado sigue en la lista de t�rminos del sistema.");
			}
		}
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un t�rmino ya existente
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo t�rmino con el nombre de uno ya existente
	 * 		+ Eliminar uno existente
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta la excepci�n de tipo: DataIntegrityViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=DataIntegrityViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testNewSpamTermThatExists() {
		// Declare variables
		Actor admin;
		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		String originalSpamTermName;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "El usuario no logueado no es un administrador.");
		
		// Execution of test
		spamTerms = spamTermService.findAll();
		
		originalSpamTermName = spamTerms.iterator().next().getTerm();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm(originalSpamTermName);
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
	
	/**
	 * Negative test case: Crear un t�rmino con user sin privilegios de administrador
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como cliente
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo t�rmino
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta la excepci�n de tipo: IllegalArgumentException
	 * 		+ Cerrar su sesi�n
	 */
	
//	org.springframework.transaction.TransactionSystemException: Could not commit JPA transaction; nested exception is javax.persistence.RollbackException: Transaction marked as rollbackOnly
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testNewSpamTermNoAdmin() {
		// Declare variables
		Actor customer;
//		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		
		// Load objects to test
		authenticate("customer1");
		customer = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(customer, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		spamTerms = spamTermService.findAll();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm("newSpamTerm");
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
	
	/**
	 * Negative test case: Crear un t�rmino con el term (nombre) vac�o
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo t�rmino con el term (nombre) vac�o
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta la excepci�n de tipo: ConstraintViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testNewSpamTermBlankName() {
		// Declare variables
		Actor admin;
//		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		spamTerms = spamTermService.findAll();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm("");
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
	
	/**
	 * Negative test case: Crear un t�rmino con el term (nombre) a null
	 * 		- Acci�n
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Traerse la lista de spam terms del sistema
	 * 		+ Crear un nuevo t�rmino con el term (nombre) a null
	 * 		- Comprobaci�n
	 * 		+ Comprobar que salta la excepci�n de tipo: ConstraintViolationException
	 * 		+ Cerrar su sesi�n
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testNewSpamTermNullName() {
		// Declare variables
		Actor admin;
//		Collection<SpamTerm> spamTerms;
		SpamTerm spamTermToAdd;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		spamTerms = spamTermService.findAll();
		
		spamTermToAdd = spamTermService.create();
		spamTermToAdd.setTerm(null);
		
		spamTermService.save(spamTermToAdd);
		
		unauthenticate();
		
		spamTermService.flush();

	}
}