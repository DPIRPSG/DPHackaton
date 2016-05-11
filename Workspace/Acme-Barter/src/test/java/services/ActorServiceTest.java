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
import domain.User;

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
	private UserService userService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Barter - Level C - 11.2
	 * Change his or her personal information.
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
		Actor user;
		User userUser;
		User userEdited;
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
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		userUser = userService.findByPrincipal();
		
		originalName = userUser.getName();
		newName = originalName + "Edited";
		userUser.setName(newName);
		
		originalSurname = userUser.getSurname();
		newSurname = originalSurname + "Edited";
		userUser.setSurname(newSurname);
		
		originalPhone = userUser.getPhone();
		newPhone = originalPhone + "Edited";
		userUser.setPhone(newPhone);
		
		originalUsername = userUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		userUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = userUser.getUserAccount().getPassword();
		newPassword = "EditedPassword";
		userUser.getUserAccount().setPassword(newPassword);
		
		userEdited = userService.saveFromOtherService(userUser);
		
		// Checks results
		Assert.isTrue(userEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
		Assert.isTrue(userEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
		Assert.isTrue(userEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
		Assert.isTrue(userEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
		Assert.isTrue(userEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
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
		Actor user;
		User userUser;
//		User userEdited;
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
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		userUser = userService.findByPrincipal();
		
//		originalName = userUser.getName();
		newName = "";
		userUser.setName(newName);
		
//		originalSurname = userUser.getSurname();
		newSurname = "";
		userUser.setSurname(newSurname);
		
//		originalPhone = userUser.getPhone();
		newPhone = "";
		userUser.setPhone(newPhone);
		
		originalUsername = userUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		userUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = userUser.getUserAccount().getPassword();
		newPassword = "EditedPassword";
		userUser.getUserAccount().setPassword(newPassword);
		
		userService.saveFromOtherService(userUser);
		
		// Checks results
//		Assert.isTrue(userEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
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
		Actor user;
		User userUser;
//		User userEdited;
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
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		userUser = userService.findByPrincipal();
		
		originalName = userUser.getName();
		newName = originalName + "Edited";
		userUser.setName(newName);
		
		originalSurname = userUser.getSurname();
		newSurname = originalSurname + "Edited";
		userUser.setSurname(newSurname);
		
		originalPhone = userUser.getPhone();
		newPhone = originalPhone + "Edited";
		userUser.setPhone(newPhone);
		
		originalUsername = userUser.getUserAccount().getUsername();
		newUsername = originalUsername + "Edited";
		userUser.getUserAccount().setUsername(newUsername);
		
//		originalPassword = userUser.getUserAccount().getPassword();
		newPassword = "pass";
		userUser.getUserAccount().setPassword(newPassword);
		
		userService.saveFromOtherService(userUser);
		
		// Checks results
//		Assert.isTrue(userEdited.getName().equals(originalName + "Edited"), "El nombre no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getSurname().equals(originalSurname + "Edited"), "El apellido no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getPhone().equals(originalPhone + "Edited"), "El teléfono no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getUserAccount().getUsername().equals(originalUsername + "Edited"), "El nombre de usuario no se ha editado correctamente.");
//		Assert.isTrue(userEdited.getUserAccount().getPassword().equals("EditedPassword"), "La contraseña no se ha editado correctamente.");
		
		unauthenticate();
		
		actorService.flush();

	}
	
}
