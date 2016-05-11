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
import domain.Autoreply;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class AutoreplyServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private AutoreplyService autoreplyService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Barter 2.0 - Level B - 2.1
	 * Manage his or her autoreplies.
	 */
	
	/**
	 * Positive test case: Listar sus autoreply
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como user
	 * 		+ Mostrar sus autoreply
	 * 		- Comprobación
	 * 		+ Comprobar que sale el número esperado de autoreply
	 */
	
	@Test 
	public void testListAutoreply() {
		// Declare variables
		Actor user;
		Collection<Autoreply> autoreplies;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreplies = autoreplyService.findByPrincipal();
		
		// Checks results
		Assert.isTrue(autoreplies.size() == 6, "El número de autoreplies listado no es el esperado");

	}
	
	/**
	 * Positive test case: Crear un nuevo Autoreply
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crear un nuevo autoreply
	 * 		- Comprobación
	 * 		+ Comprobar que el numero nuevo de autoreplies es el mismo que antes más 1
	 */
	
	@Test 
	public void testCreateAutoreply() {
		// Declare variables
		Actor user;
		Collection<Autoreply> autoreplies;
		Integer numberOfAutoreplies;
		Collection<Autoreply> newAutoreplies;
		Integer newNumberOfAutoreplies;
		Autoreply autoreply;
		Collection<String> keyWords;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreplies = autoreplyService.findByPrincipal();
		numberOfAutoreplies = autoreplies.size();
		
		autoreply = autoreplyService.create();
		
		keyWords = new ArrayList<>();
		
		keyWords.add("esto");
		keyWords.add("es");
		keyWords.add("un");
		keyWords.add("test");
		
		autoreply.setKeyWords(keyWords);
		
		autoreply.setText("Hola, esto es un mensaje de prueba para un test.");
		
		autoreplyService.saveFromEdit(autoreply);
		
		// Checks results
		newAutoreplies = autoreplyService.findByPrincipal();
		newNumberOfAutoreplies = newAutoreplies.size();
		
		Assert.isTrue(newNumberOfAutoreplies == numberOfAutoreplies + 1, "El número de autoreplies listado no es el que había antes + 1");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un nuevo Autoreply sin keyWords
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crear un nuevo autoreply sin keyWords
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test 
	public void testCreateAutoreplyNoKeyWords() {
		// Declare variables
		Actor user;
//		Collection<Autoreply> autoreplies;
//		Integer numberOfAutoreplies;
//		Collection<Autoreply> newAutoreplies;
//		Integer newNumberOfAutoreplies;
		Autoreply autoreply;
//		Collection<String> keyWords;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		autoreplies = autoreplyService.findByPrincipal();
//		numberOfAutoreplies = autoreplies.size();
		
		autoreply = autoreplyService.create();
		
//		keyWords = new ArrayList<>();
//		
//		keyWords.add("esto");
//		keyWords.add("es");
//		keyWords.add("un");
//		keyWords.add("test");
//		
//		autoreply.setKeyWords(keyWords);
		
		autoreply.setText("Hola, esto es un mensaje de prueba para un test.");
		
		autoreplyService.saveFromEdit(autoreply);
		
		// Checks results
//		newAutoreplies = autoreplyService.findByPrincipal();
//		newNumberOfAutoreplies = newAutoreplies.size();
//		
//		Assert.isTrue(newNumberOfAutoreplies == numberOfAutoreplies + 1, "El número de autoreplies listado no es el que había antes + 1");
//		
		unauthenticate();
		
		autoreplyService.flush();

	}
	
	/**
	 * Negative test case: Crear un nuevo Autoreply sin text
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crear un nuevo autoreply sin text
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test 
	public void testCreateAutoreplyNoText() {
		// Declare variables
		Actor user;
//		Collection<Autoreply> autoreplies;
//		Integer numberOfAutoreplies;
//		Collection<Autoreply> newAutoreplies;
//		Integer newNumberOfAutoreplies;
		Autoreply autoreply;
		Collection<String> keyWords;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		autoreplies = autoreplyService.findByPrincipal();
//		numberOfAutoreplies = autoreplies.size();
		
		autoreply = autoreplyService.create();
		
		keyWords = new ArrayList<>();
		
		keyWords.add("esto");
		keyWords.add("es");
		keyWords.add("un");
		keyWords.add("test");
		
		autoreply.setKeyWords(keyWords);
		
//		autoreply.setText("Hola, esto es un mensaje de prueba para un test.");
		
		autoreplyService.saveFromEdit(autoreply);
		
		// Checks results
//		newAutoreplies = autoreplyService.findByPrincipal();
//		newNumberOfAutoreplies = newAutoreplies.size();
//		
//		Assert.isTrue(newNumberOfAutoreplies == numberOfAutoreplies + 1, "El número de autoreplies listado no es el que había antes + 1");
//		
		unauthenticate();
		
		autoreplyService.flush();

	}
	
	/**
	 * Positive test case: Editar un autoreply
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como user
	 * 		+ Editar un Autoreply
	 * 		- Comprobación
	 * 		+ Comprobar que los datos del Autoreply son ahora los nuevos.
	 */
	
	@Test 
	public void testEditAutoreply() {
		// Declare variables
		Actor user;
		Collection<Autoreply> autoreplies;
		Autoreply autoreply;
		Integer autoreplyId;
		Collection<String> keyWords;
		Autoreply newAutoreply;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreplies = autoreplyService.findByPrincipal();
		autoreply = autoreplies.iterator().next();
		autoreplyId = autoreply.getId();
		
		keyWords = new ArrayList<>();
		
		keyWords.add("KEY");
		keyWords.add("WORDS");
		keyWords.add("DE");
		keyWords.add("PRUEBA");
		
		autoreply.setKeyWords(keyWords);
		
		autoreply.setText("TEXT DE PRUEBA");
		
		autoreplyService.saveFromEdit(autoreply);
		
		// Checks results
		newAutoreply = autoreplyService.findOne(autoreplyId);
		
		Assert.isTrue(newAutoreply.getKeyWords().size() == keyWords.size(), "Los keyWords no se han editado correctamente: El tamaño de la lista anterior y la nueva no es el mismo.");
		
		Boolean keyWordsEdited;
		keyWordsEdited = true;
		
		for(String k: newAutoreply.getKeyWords()){
			if(!keyWords.contains(k)){
				keyWordsEdited = false;
			}
		}
		
		Assert.isTrue(keyWordsEdited, "Los keyWords no se han editado correctamente: No contiene alguno de los keyWords nuevos.");
		
		Assert.isTrue(newAutoreply.getText().equals("TEXT DE PRUEBA"), "El text no se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Editar un autoreply de otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como user
	 * 		+ Editar un Autoreply, cambiando el id por el id de un autoreply de otro usuario
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testEditAutoreplyOfOtherUser() {
		// Declare variables
		Actor user;
//		Collection<Autoreply> autoreplies;
		Autoreply autoreply;
//		Integer autoreplyId;
		Collection<Autoreply> otherAutoreplies;
		Autoreply otherAutoreply;
		Integer otherAutoreplyId;
		Collection<String> keyWords;
		Autoreply newAutoreply;
		
		// Load objects to test
		authenticate("user2");
		otherAutoreplies = autoreplyService.findByPrincipal();
		otherAutoreply = otherAutoreplies.iterator().next();
		otherAutoreplyId = otherAutoreply.getId();
		
		unauthenticate();
		
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreply = autoreplyService.create();
		
		autoreply.setId(otherAutoreplyId);
		
		keyWords = new ArrayList<>();
		
		keyWords.add("KEY");
		keyWords.add("WORDS");
		keyWords.add("DE");
		keyWords.add("PRUEBA");
		
		autoreply.setKeyWords(keyWords);
		
		autoreply.setText("TEXT DE PRUEBA");
		
		autoreplyService.saveFromEdit(autoreply);
		
		// Checks results
		newAutoreply = autoreplyService.findOne(otherAutoreplyId);
		
//		Assert.isTrue(newAutoreply.getKeyWords().size() == keyWords.size(), "Los keyWords no se han editado correctamente: El tamaño de la lista anterior y la nueva no es el mismo.");
		
		Integer keyWordsEditedCounter;
		keyWordsEditedCounter = 0;
		
		for(String k: newAutoreply.getKeyWords()){
			if(keyWords.contains(k)){
				keyWordsEditedCounter++;
			}
		}
		
		Assert.isTrue(keyWordsEditedCounter == newAutoreply.getKeyWords().size(), "Los keyWords se han editado correctamente: Contiene exactamente todos de los keyWords nuevos.");
		
		Assert.isTrue(!newAutoreply.getText().equals("TEXT DE PRUEBA"), "El text se han editado correctamente.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Editar un autoreply asignándoselo a otro usuario (Crear un autoreply a otro usuario)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como user
	 * 		+ Editar un Autoreply, cambiando el id del actor por el de otro usuario
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testEditAutoreplyForOtherUser() {
		// Declare variables
		Actor user;
		Collection<Autoreply> autoreplies;
		Autoreply autoreply;
		Integer autoreplyId;
		Collection<String> keyWords;
		Actor otherUser;
		Autoreply newAutoreply;
		
		// Load objects to test
		authenticate("user2");
		otherUser = actorService.findByPrincipal();
		
		unauthenticate();
		
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreplies = autoreplyService.findByPrincipal();
		autoreply = autoreplies.iterator().next();
		autoreplyId = autoreply.getId();
		
		autoreply.setActor(otherUser);
		
		keyWords = new ArrayList<>();
		
		keyWords.add("KEY");
		keyWords.add("WORDS");
		keyWords.add("DE");
		keyWords.add("PRUEBA");
		
		autoreply.setKeyWords(keyWords);
		
		autoreply.setText("TEXT DE PRUEBA");
		
		autoreplyService.saveFromEdit(autoreply);
		
		// Checks results
		newAutoreply = autoreplyService.findOne(autoreplyId);
		
		Assert.isTrue(newAutoreply.getActor() == actorService.findByPrincipal(), "Se ha editado correctamente el usuario.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Eliminar un autoreply
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como user
	 * 		+ Eliminar un Autoreply
	 * 		- Comprobación
	 * 		+ Comprobar que el numero de autoreplies de ahora es el de antes menos 1
	 */
	
	@Test 
	public void testDeleteAutoreply() {
		// Declare variables
		Actor user;
		Collection<Autoreply> autoreplies;
		Autoreply autoreply;
		Collection<Autoreply> newAutoreplies;
		Integer numberOfAutoreplies;
		Integer newNumberOfAutoreplies;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreplies = autoreplyService.findByPrincipal();
		numberOfAutoreplies = autoreplies.size();
		autoreply = autoreplies.iterator().next();
		
		autoreplyService.deleteFromEdit(autoreply);
		
		// Checks results
		newAutoreplies = autoreplyService.findByPrincipal();
		newNumberOfAutoreplies = newAutoreplies.size();
		
		Assert.isTrue(newNumberOfAutoreplies == numberOfAutoreplies - 1 , "Ahora no hay 1 Autoreply menos que antes.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un autoreply de otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como user
	 * 		+ Eliminar un Autoreply
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testDeleteAutoreplyOfOtherUser() {
		// Declare variables
		Actor user;
		Autoreply autoreply;
		Collection<Autoreply> otherAutoreplies;
		Autoreply otherAutoreply;
		Integer otherAutoreplyId;
		
		// Load objects to test
		authenticate("user2");
		otherAutoreplies = autoreplyService.findByPrincipal();
		otherAutoreply = otherAutoreplies.iterator().next();
		otherAutoreplyId = otherAutoreply.getId();
		
		unauthenticate();
		
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		autoreply = autoreplyService.findOne(otherAutoreplyId);
		
		autoreplyService.deleteFromEdit(autoreply);
		
		// Checks results
		
		unauthenticate();

	}
	

}
