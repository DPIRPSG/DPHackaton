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

import domain.LegalText;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class LegalTextServiceTest extends AbstractTest{

	// Service under test -------------------------
	@Autowired
	private LegalTextService legalTextService;
	
	// Other services needed -----------------------

	// Tests ---------------------------------------

	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Positive test case: Se muestran los legalText.
	 * 
	 */
	@Test
	public void testLegalTextFindAll1(){
		// Declare variable
		Collection<LegalText> result;
		
		// Load objects to test
		authenticate("admin");
		
		// Check basic requirements
		
		// Execution of test
		result = legalTextService.findAll();
		
		// Check results
		Assert.isTrue(result.size() == 6);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Positive test case: Se crea un legalText.
	 * 
	 */
	@Test
	public void testLegalTextCreate1(){
		// Declare variable
		LegalText result;
		String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("admin");
		text = "New Legal Text";
		allLegalTexts = legalTextService.findAll();
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		result = legalTextService.create();
		result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 7);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se crea un legalText porque no estás autenticado.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testLegalTextCreate2(){
		// Declare variable
		LegalText result;
		String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		//authenticate("admin");
		text = "New Legal Text";
		allLegalTexts = legalTextService.findAll();
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		result = legalTextService.create();
		result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 7);
		//authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se crea un legalText porque estás autenticado como user.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testLegalTextCreate3(){
		// Declare variable
		LegalText result;
		String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("user1");
		text = "New Legal Text";
		allLegalTexts = legalTextService.findAll();
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		result = legalTextService.create();
		result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 7);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se crea un legalText porque no le pasas el text.
	 * 
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
	public void testLegalTextCreate4(){
		// Declare variable
		LegalText result;
		//String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("admin");
		//text = "New Legal Text";
		allLegalTexts = legalTextService.findAll();
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		result = legalTextService.create();
		//result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 7);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Positive test case: Se edita un legalText.
	 * 
	 */
	@Test
	public void testLegalTextEdit1(){
		// Declare variable
		LegalText result = null;
		String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("admin");
		text = "Edited Legal Text";
		allLegalTexts = legalTextService.findAll();
		result = allLegalTexts.iterator().next();
		
		// Check basic requirements
		
		// Execution of test
		result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		for(LegalText l:allLegalTexts){
			if(l.getId() == result.getId()){
				result = l;
				break;
			}
		}
		Assert.isTrue(result.getText() == text);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se edita un legalText porque no eres un admin.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testLegalTextEdit2(){
		// Declare variable
		LegalText result = null;
		String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		//authenticate("admin");
		text = "Edited Legal Text";
		allLegalTexts = legalTextService.findAll();
		result = allLegalTexts.iterator().next();
		
		// Check basic requirements
		
		// Execution of test
		result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		for(LegalText l:allLegalTexts){
			if(l.getId() == result.getId()){
				result = l;
				break;
			}
		}
		Assert.isTrue(result.getText() == text);
		//authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se edita un legalText porque le pasas una cadena vacía.
	 * 
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
	public void testLegalTextEdit3(){
		// Declare variable
		LegalText result = null;
		String text;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("admin");
		text = "";
		allLegalTexts = legalTextService.findAll();
		result = allLegalTexts.iterator().next();
		
		// Check basic requirements
		
		// Execution of test
		result.setText(text);
		legalTextService.save(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		for(LegalText l:allLegalTexts){
			if(l.getId() == result.getId()){
				result = l;
				break;
			}
		}
		Assert.isTrue(result.getText() == text);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Positive test case: Se borra un legalText.
	 * 
	 */
	@Test
	public void testLegalTextDelete1(){
		// Declare variable
		LegalText result = null;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("admin");
		allLegalTexts = legalTextService.findAll();
		for(LegalText l:allLegalTexts){
			if(l.getText().equals("Queda sujeto a las condiciones del auditor del Emparejamiento, Acme Barter Inc. (y cualquiera de sus auditores) se reserva el derecho de controlar y/o cancelar cualquier intercambio sin previo aviso.")){
				result = l;
				break;
			}
		}
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		legalTextService.delete(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 5);
		authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Barter - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se borra un legalText porque no eres un admin.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
	public void testLegalTextDelete2(){
		// Declare variable
		LegalText result = null;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		//authenticate("admin");
		allLegalTexts = legalTextService.findAll();
		for(LegalText l:allLegalTexts){
			if(l.getId() == 130){
				result = l;
			}
		}
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		legalTextService.delete(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 5);
		//authenticate(null);
		legalTextService.flush();
	}
	
	/**
	 * Acme-Six-Pack - Level C - 12.2
	 * An actor who is authenticated as an administrator must be able to:
	 * Manage the collection of legal texts that are available to the users.
	 * 
	 * Negative test case: No se borra un legalText porque está asignado a un match.
	 * 
	 */
	@Test(expected=DataIntegrityViolationException.class)
	@Rollback(value = true)
	public void testLegalTextDelete3(){
		// Declare variable
		LegalText result = null;
		Collection<LegalText> allLegalTexts;
		
		// Load objects to test
		authenticate("admin");
		allLegalTexts = legalTextService.findAll();
		result = allLegalTexts.iterator().next();
		
		// Check basic requirements
		Assert.isTrue(allLegalTexts.size() == 6);
		
		// Execution of test
		legalTextService.delete(result);
		
		// Check results
		allLegalTexts = legalTextService.findAll();
		Assert.isTrue(allLegalTexts.size() == 5);
		authenticate(null);
		legalTextService.flush();
	}
	
}
