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

import domain.Actor;
import domain.Runner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ActorServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private ActorService actorService;
	
	// Other services needed -----------------------
	
	@Autowired
	private RunnerService runnerService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Cambiar la información de su perfil.
	 */
	
	/**
	 * Positive test case: Modificar sus datos del perfil
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Editar sus datos de perfil
	 * 		- Comprobación
	 * 		+ Comprobar que los datos de su perfil son ahora los nuevos que ha introducido
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testEditProfile() {
		// Declare variables
		Actor runner;
		Runner runnerUser;
		Runner runnerEdited;
		String originalName;
		String originalSurname;
		String originalPhone;
		String originalUsername;
//		String originalPassword;
		String newName;
		String newSurname;
		String newPhone;
		String newUsername;
		String newPassword;
		
		// Load objects to test
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(runner, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		runnerUser = runnerService.findByPrincipal();
		
		originalName = runnerUser.getName();
		newName = originalName + "Edited";
		runnerUser.setName(newName);
		
		originalSurname = runnerUser.getSurname();
		newSurname = originalSurname + "Edited";
		runnerUser.setSurname(newSurname);
		
		originalPhone = runnerUser.getPhone();
		newPhone = originalPhone + "Edited";
		runnerUser.setPhone(newPhone);
		
		originalUsername = runnerUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		runnerUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = runnerUser.getUserAccount().getPassword();
		newPassword = "EditedPassword";
		runnerUser.getUserAccount().setPassword(newPassword);
		
		runnerEdited = runnerService.saveFromEdit(runnerUser);
		
		// Checks results
		Assert.isTrue(runnerEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
		Assert.isTrue(runnerEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
		Assert.isTrue(runnerEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
		Assert.isTrue(runnerEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
		Assert.isTrue(runnerEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}
	
	/**
	 * Negative test case: Modificar sus datos del perfil dejando campos en blanco
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Editar sus datos de perfil
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testEditProfileBlankFields() {
		// Declare variables
		Actor runner;
		Runner runnerUser;
//		Runner runnerEdited;
//		String originalName;
//		String originalSurname;
//		String originalPhone;
		String originalUsername;
//		String originalPassword;
		String newName;
		String newSurname;
		String newPhone;
		String newUsername;
		String newPassword;
		
		// Load objects to test
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(runner, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		runnerUser = runnerService.findByPrincipal();
		
//		originalName = runnerUser.getName();
		newName = "";
		runnerUser.setName(newName);
		
//		originalSurname = runnerUser.getSurname();
		newSurname = "";
		runnerUser.setSurname(newSurname);
		
//		originalPhone = runnerUser.getPhone();
		newPhone = "";
		runnerUser.setPhone(newPhone);
		
		originalUsername = runnerUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		runnerUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = runnerUser.getUserAccount().getPassword();
		newPassword = "EditedPassword";
		runnerUser.getUserAccount().setPassword(newPassword);
		
		runnerService.saveFromEdit(runnerUser);
		
		// Checks results
//		Assert.isTrue(runnerEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}
	
	/**
	 * Negative test case: Modificar sus datos del perfil con contraseña demasiado corta
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Editar sus datos de perfil eligiendo una contraseña demasiado corta
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testEditProfileShortPassword() {
		// Declare variables
		Actor runner;
		Runner runnerUser;
//		Runner runnerEdited;
		String originalName;
		String originalSurname;
		String originalPhone;
		String originalUsername;
//		String originalPassword;
		String newName;
		String newSurname;
		String newPhone;
		String newUsername;
		String newPassword;
		
		// Load objects to test
		authenticate("runner1");
		runner = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(runner, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		runnerUser = runnerService.findByPrincipal();
		
		originalName = runnerUser.getName();
		newName = originalName + "Edited";
		runnerUser.setName(newName);
		
		originalSurname = runnerUser.getSurname();
		newSurname = originalSurname + "Edited";
		runnerUser.setSurname(newSurname);
		
		originalPhone = runnerUser.getPhone();
		newPhone = originalPhone + "Edited";
		runnerUser.setPhone(newPhone);
		
		originalUsername = runnerUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		runnerUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = runnerUser.getUserAccount().getPassword();
		newPassword = "pass";
		runnerUser.getUserAccount().setPassword(newPassword);
		
		runnerService.saveFromEdit(runnerUser);
		
		// Checks results
//		Assert.isTrue(runnerEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
//		Assert.isTrue(runnerEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}

}
