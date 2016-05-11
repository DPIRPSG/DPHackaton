package services;

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
import domain.Folder;
import domain.Message;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class FolderServiceTest extends AbstractTest {
	
	// Service under test -------------------------

	@Autowired
	private FolderService folderService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Barter - Level C - 10.3
	 * Actors can create additional folders, rename, or delete them.
	 */
	
	/**
	 * Positive test case: Crear un nuevo folder
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el dueño de la carpeta creada es el actor logueado
	 * 		+ Comprobar que aparece la nueva carpeta creada
	 * 		+ Comprobar que tiene la cantidad de carpetas de antes más 1
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewFolder() {
		// Declare variables
		Actor user;
		Folder folder;
		Folder newFolder;
		Collection<Folder> actorFolders;
		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(false);
		folder.setActor(user);
		
		newFolder = folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
		folderService.checkActor(newFolder); // First check
		
		actorFolders = folderService.findAllByActor();
		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
		
		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Crear un nuevo folder a otro usuario del sistema (Test planteado como negativo, pero resultante como positivo al no saltar excepción, si no autocorregirse para, aunque tú impongas otro usuario que no seas tú, te inserte el usuario logueado como dueño de la carpeta)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crear una carpeta
	 * 		+ Asignarsela a otro usuario del sistema
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el dueño de la carpeta es el del creador y no el usuario al que se intenta crear
	 * 		+ Comprobar que entre las carpetas del usuario creador se encuentra la nueva carpeta creada
	 * 		+ Comprobar que el número de carpetas del usuario creador es el mismo de antes + 1
	 * 		+ Comprobar que entre las carpetas del usuario al que se intenta crear no se encuentra la nueva carpeta creada
	 * 		+ Comprobar que el número de carpetas del usuario al que se intentar crear la carpeta no ha cambiado
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewFolderForAnotherUser() {
		// Declare variables
		Actor user;
		Actor otherUser;
		Folder folder;
		Folder newFolder;
		Collection<Folder> actorFolders;
		Collection<Folder> otherActorFolders;
		Integer numberOfFolders;
		Integer numberOfFoldersOtherActor;
		Integer actorId;
		Collection<User> users;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		users = userService.findAll();
		
		otherUser = null;
		
		for(User u: users){
			if(u.getUserAccount().getUsername().equals("user2")){
				otherUser = u;
			}
		}
		
		Assert.notNull(otherUser, "No existe el otro usuario con el que se pretende testar.");
		
		actorId = otherUser.getId();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		numberOfFolders = folderService.findAllByActor().size();
		numberOfFoldersOtherActor = folderService.findAllByActorId(actorId).size();
		
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(false);
		folder.setActor(otherUser);
		
		newFolder = folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
		folderService.checkActor(newFolder); // First check
		
		actorFolders = folderService.findAllByActor();
		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
		
		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta."); // Third check
		
		otherActorFolders = folderService.findAllByActorId(actorId);
		
		Assert.isTrue(!otherActorFolders.contains(newFolder), "El usuario al que se le ha intentado crear una carpeta desde otra cuenta tiene asignada la carpeta, cuando no debería."); // Fourth check
		
		Assert.isTrue(folderService.findAllByActorId(actorId).size() == numberOfFoldersOtherActor, "El actor al que se le ha intentado crear una carpeta desde otra cuenta no tiene el mismo número de carpetas que antes tras crearse una nueva carpeta."); // Fifth check
		
		
		unauthenticate();

	}
	
	
	
	/**
	 * Positive test case: Crear un nuevo folder como folder del sistema (isSystem = true) (Test planteado como negativo, pero resultante como positivo al no saltar excepción, si no autocorregirse poniendo siempre el isSystem a true aunque tú se lo pases como false)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		+ Hacerla carpeta del sistema
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que la carpeta creada tiene el isSytem a false
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewFolderIsSystem() {
		// Declare variables
		Actor user;
		Folder folder;
		Folder newFolder;
//		Collection<Folder> actorFolders;
//		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName("Nueva carpeta");
		folder.setIsSystem(true);
		folder.setActor(user);
		
		newFolder = folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
		Assert.isTrue(newFolder.getIsSystem() == false, "Se ha conseguido crear una carpeta del sistema.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Crear un nuevo folder con el nombre vacío
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test 
	public void testNewBlankNameFolder() {
		// Declare variables
		Actor user;
		Folder folder;
//		Folder newFolder;
//		Collection<Folder> actorFolders;
//		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName("");
		folder.setIsSystem(false);
		folder.setActor(user);
		
		folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
//		folderService.checkActor(newFolder); // First check
//		
//		actorFolders = folderService.findAllByActor();
//		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
//		
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();
		
		folderService.flush();

	}
	
	/**
	 * Negative test case: Crear un nuevo folder con el nombre a null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Crearse una carpeta
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test 
	public void testNewNullNameFolder() {
		// Declare variables
		Actor user;
		Folder folder;
//		Folder newFolder;
//		Collection<Folder> actorFolders;
//		Integer numberOfFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfFolders = folderService.findAllByActor().size();
		
		folder = folderService.create();
		folder.setName(null);
		folder.setIsSystem(false);
		folder.setActor(user);
		
		folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
//		folderService.checkActor(newFolder); // First check
//		
//		actorFolders = folderService.findAllByActor();
//		Assert.isTrue(actorFolders.contains(newFolder), "El usuario no tiene asignada la carpeta que acaba de crearse."); // Second check
//		
//		Assert.isTrue(folderService.findAllByActor().size() == numberOfFolders + 1, "El actor no tiene el mismo número de carpetas que antes + 1 tras crearse una nueva carpeta"); // Third check
		
		unauthenticate();
		
		folderService.flush();

	}
	
	/**
	 * Positive test case: Renombrar un folder
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta a "Carpeta renombrada"
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que el dueño de la carpeta renombrada es el actor logueado
	 * 		* Comprobar que el actor logueado posee dicha carpeta
	 * 		+ Comprobar que no existe carpeta creada con el nombre que tenía la carpeta renombrada
	 * 		+ Comprobar que existe una carpeta con el nombre "Carpeta renombrada"
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testRenameFolder() {
		// Declare variables
		Actor user;
		Folder folder;
		Folder renamedFolder;
		Collection<Folder> actorFolders;
		Collection<Folder> newActorFolders;
		String oldFolderName;
		Boolean existNewFolderName;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		existNewFolderName = false;
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		folder = actorFolders.iterator().next();
		oldFolderName = folder.getName();
		
		folder.setName("Carpeta renombrada");
		
		renamedFolder = folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
		folderService.checkActor(renamedFolder); // First check
		
		newActorFolders = folderService.findAllByActor();
		Assert.isTrue(newActorFolders.contains(renamedFolder), "El usuario no tiene asignada la carpeta que acaba de renombrar."); // Second check
				
		for(Folder f: newActorFolders){
			if(f.getName() == oldFolderName){
				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que tenía antes de ser renombrada."); // Third check
			}
			if(f.getName() == "Carpeta renombrada"){
				existNewFolderName = true;
			}
		}
		
		Assert.isTrue(existNewFolderName, "El actor logueado no tiene ninguna carpeta con el nombre (\"Carpeta renombrada\") que se le ha dado a la carpeta renombrada."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Renombrar un folder de otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta de otro usuario a "Carpeta renombrada"
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testRenameFolderOfOther() {
		// Declare variables
		Actor user;
		Folder folder;
//		Folder renamedFolder;
//		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String oldFolderName;
//		Boolean existNewFolderName;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
//		existNewFolderName = false;
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		folder = folderService.findOne(87); // Id de la carpeta InBox del user2
//		oldFolderName = folder.getName();
		
		folder.setName("Carpeta renombrada");
		
		folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
//		folderService.checkActor(renamedFolder); // First check
//		
//		newActorFolders = folderService.findAllByActor();
//		Assert.isTrue(newActorFolders.contains(renamedFolder), "El usuario no tiene asignada la carpeta que acaba de renombrar."); // Second check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == oldFolderName){
//				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que tenía antes de ser renombrada."); // Third check
//			}
//			if(f.getName() == "Carpeta renombrada"){
//				existNewFolderName = true;
//			}
//		}
//		
//		Assert.isTrue(existNewFolderName, "El actor logueado no tiene ninguna carpeta con el nombre (\"Carpeta renombrada\") que se le ha dado a la carpeta renombrada."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Renombrar un folder a un texto vacío
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Renombrar una carpeta a ""
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		+ Comprobar que salta una excepción del tipo: ConstraintViolationException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value = true)
//	@Test
	public void testRenameFolderToBlank() {
		// Declare variables
		Actor user;
		Folder folder;
//		Folder renamedFolder;
		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String oldFolderName;
//		Boolean existNewFolderName;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
//		existNewFolderName = false;
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		folder = actorFolders.iterator().next();
//		oldFolderName = folder.getName();
		
		folder.setName("");
		
		folderService.saveFromCreateOrEdit(folder);
		
		// Checks results
//		folderService.checkActor(renamedFolder); // First check
//		
//		newActorFolders = folderService.findAllByActor();
//		Assert.isTrue(newActorFolders.contains(renamedFolder), "El usuario no tiene asignada la carpeta que acaba de renombrar."); // Second check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == oldFolderName){
//				Assert.isTrue(false, "La carpeta sigue teniendo el nombre que tenía antes de ser renombrada."); // Third check
//			}
//			if(f.getName() == "Carpeta renombrada"){
//				existNewFolderName = true;
//			}
//		}
//		
//		Assert.isTrue(existNewFolderName, "El actor logueado no tiene ninguna carpeta con el nombre (\"Carpeta renombrada\") que se le ha dado a la carpeta renombrada."); // Fourth check
		
		unauthenticate();
		
		folderService.flush();

	}
	
	/**
	 * Positive test case: Eliminar un folder
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta suya
	 * 		- Comprobación
	 * 		+ Listar sus carpetas
	 * 		* Comprobar que el actor logueado ya no posee dicha carpeta
	 * 		+ Comprobar que no existe carpeta creada con el nombre que tenía la carpeta eliminada
	 * 		+ Comprobar que el número de carpetas que posee es el mismo que antes - 1
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteFolder() {
		// Declare variables
		Actor user;
		Folder folder;
		Collection<Folder> actorFolders;
		Collection<Folder> newActorFolders;
		String folderName;
		Integer numberOfFolders;
		Integer newNumberOfFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		numberOfFolders = actorFolders.size();
		
		folder = null;
		for(Folder f: actorFolders){
			if(f.getName().equals("MyBox")){
				folder = f;
			}
		}
		folderName = folder.getName();
		
		folderService.delete(folder);
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		newNumberOfFolders = newActorFolders.size();
		
		Assert.isTrue(!newActorFolders.contains(folder), "El usuario sigue teniendo asignada la carpeta que acaba de eliminar."); // First check
				
		for(Folder f: newActorFolders){
			if(f.getName() == folderName){
				Assert.isTrue(false, "El actor sigue teniendo una carpeta con el nombre de la carpeta que acaba de borrar."); // Second check
			}
		}
				
		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo número de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un folder del sistema
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta suya, pero que sea carpeta del sistema
	 * 		- Comprobación
	 * 		* Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testDeleteSystemFolder() {
		// Declare variables
		Actor user;
		Folder folder;
		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String folderName;
//		Integer numberOfFolders;
//		Integer newNumberOfFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
//		numberOfFolders = actorFolders.size();
		
		folder = actorFolders.iterator().next(); // folder es una carpeta del sistema
//		folderName = folder.getName();
		while(folder.getIsSystem() == false){
			folder = actorFolders.iterator().next();
		}
		
		folderService.delete(folder);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		newNumberOfFolders = newActorFolders.size();
//		
//		Assert.isTrue(newActorFolders.contains(folder), "El usuario sigue teniendo asignada la carpeta que acaba de eliminar."); // First check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == folderName){
//				Assert.isTrue(false, "El actor sigue teniendo una carpeta con el nombre de la carpeta que acaba de borrar."); // Second check
//			}
//		}
//				
//		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo número de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Eliminar un folder de otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Eliminar una carpeta que no sea tuya
	 * 		- Comprobación
	 * 		* Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testDeleteSystemFolderOfOtherUser() {
		// Declare variables
		Actor user;
		Folder folder;
//		Collection<Folder> actorFolders;
//		Collection<Folder> newActorFolders;
//		String folderName;
//		Integer numberOfFolders;
//		Integer newNumberOfFolders;
		
		// Load objects to test
		authenticate("user2");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		actorFolders = folderService.findAllByActor();
//		numberOfFolders = actorFolders.size();
		
		folder = folderService.findOne(122); // Carpeta "MyBox", no del sistema, del user1
//		folderName = folder.getName();
//		while(folder.getIsSystem() == false){
//			folder = actorFolders.iterator().next();
//		}
		
		folderService.delete(folder);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		newNumberOfFolders = newActorFolders.size();
//		
//		Assert.isTrue(newActorFolders.contains(folder), "El usuario sigue teniendo asignada la carpeta que acaba de eliminar."); // First check
//				
//		for(Folder f: newActorFolders){
//			if(f.getName() == folderName){
//				Assert.isTrue(false, "El actor sigue teniendo una carpeta con el nombre de la carpeta que acaba de borrar."); // Second check
//			}
//		}
//				
//		Assert.isTrue( (numberOfFolders - 1 == newNumberOfFolders) , "El actor no tiene el mismo número de carpetas que antes - 1 al borrar una de sus carpetas."); // Fourth check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level C - 10.3
	 * When a message is deleted from a folder other than "trash box", it is moved to "trash box"
	 */
	
	/**
	 * Positive test case: Borrar un mensaje de una carpeta
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Entrar a una carpeta
	 * 		+ Borrar un mensaje de ella
	 * 		- Comprobación
	 * 		+ Entrar a la carpeta
	 * 		+ Comprobar que el mensaje no sigue en esa carpeta
	 * 		+ Comprobar que el mensaje está ahora en la carpeta TrashBox
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteMessage() {
		// Declare variables
		Actor user;
		Folder folder;
		Collection<Folder> actorFolders;
		Message message;
		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: actorFolders){
			if(f.getName().equals("InBox")){
				folder = f;
			}
		}
		Assert.notNull(folder, "No está el folder \"InBox\" necesario para realizar el test.");
		
		message = null;
		for(Message m: folder.getMessages()){
			message = m;
		}
		Assert.notNull(message, "No hay ningún mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(folder, message);
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: newActorFolders){
			if(f.getName().equals("InBox")){
				folder = f;
			}
		}
		
		Assert.isTrue(!folder.getMessages().contains(message), "La carpeta todavía contiene el mensaje eliminado."); // First check
		
		folder = null;
		for(Folder f: newActorFolders){
			if(f.getName().equals("TrashBox")){
				folder = f;
			}
		}
		
		Assert.isTrue(folder.getMessages().contains(message), "La carpeta TrashBox no contiene el mensaje eliminado."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level C - 10.3
	 * when it is deleted from "trash box", it is actually removed from the system.
	 */
	
	/**
	 * Positive test case: Borrar un mensaje de la carpeta TrashBox
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Entrar a la carpeta TrashBox
	 * 		+ Borrar un mensaje de ella
	 * 		- Comprobación
	 * 		+ Entrar a la carpeta
	 * 		+ Comprobar que el mensaje no está ahora en la carpeta TrashBox
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testDeleteMessageFromTrashBox() {
		// Declare variables
		Actor user;
		Folder folder;
		Collection<Folder> actorFolders;
		Message message;
		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: actorFolders){
			if(f.getName().equals("TrashBox")){
				folder = f;
			}
		}
		Assert.notNull(folder, "No está el folder \"TrashBox\" necesario para realizar el test.");
		
		message = null;
		for(Message m: folder.getMessages()){
			message = m;
		}
		Assert.notNull(message, "No hay ningún mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(folder, message);
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		
		folder = null;
		for(Folder f: newActorFolders){
			if(f.getName().equals("TrashBox")){
				folder = f;
			}
		}
		
		Assert.isTrue(!folder.getMessages().contains(message), "La carpeta TrashBox sigue conteniendo el mensaje eliminado."); // First check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Borrar un mensaje de otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Borrar un mensaje de otro usuario
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testDeleteMessageOfOther() {
		// Declare variables
		Actor user;
		Folder folder;
//		Collection<Folder> actorFolders;
		Message message;
//		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		actorFolders = folderService.findAllByActor();
		
		folder = folderService.findOne(87); // Carpeta InBox del user2
		Assert.notNull(folder, "No está el folder necesario para realizar el test.");
		
		message = null;
		for(Message m: folder.getMessages()){
			message = m;
		}
		Assert.notNull(message, "No hay ningún mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(folder, message);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(!folder.getMessages().contains(message), "La carpeta todavía contiene el mensaje eliminado."); // First check
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("TrashBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(folder.getMessages().contains(message), "La carpeta TrashBox no contiene el mensaje eliminado."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Borrar un mensaje null de una carpeta null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Borrar un mensaje null de una carpeta null
	 * 		- Comprobación
	 * 		+ Entrar a la carpeta
	 * 		+ Comprobar que salta una execpción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testDeleteNullMessageFromNullFolder() {
		// Declare variables
		Actor user;
//		Folder folder;
//		Folder otherFolder;
//		Collection<Folder> actorFolders;
//		Message message;
//		Collection<Folder> newActorFolders;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		actorFolders = folderService.findAllByActor();
//		
//		folder = null;
//		for(Folder f: actorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
//		}
//		Assert.notNull(folder, "No está el folder \"InBox\" necesario para realizar el test.");
//		
//		otherFolder = null;
//		for(Folder f: actorFolders){
//			if(f.getName().equals("OutBox")){
//				otherFolder = f;
//			}
//		}
//		Assert.notNull(otherFolder, "No está el folder \"OutBox\" necesario para realizar el test.");
//		
//		message = null;
//		for(Message m: folder.getMessages()){
//			message = m;
//		}
//		Assert.notNull(message, "No hay ningún mensaje en la carpeta, necesario para realizar el test.");
		
		folderService.removeMessage(null, null);
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(folder.getMessages().contains(message), "La carpeta ya no contiene el mensaje, y debería."); // First check
//		
//		folder = null;
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("TrashBox")){
//				folder = f;
//			}
//		}
//		
//		Assert.isTrue(!folder.getMessages().contains(message), "El mensaje ha sido enviado a TrashBox sin haber sido eliminado de ninguna carpeta."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level C - 10.3
	 * Actors can flag their messages as spam, in which case they are moved automatically to the "spam box" folder.
	 */
	
	/**
	 * Positive test case: Marcar un mensaje como Spam
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Acceder a una de sus carpetas
	 * 		+ Marcar uno de sus mensajes como Spam
	 * 		- Comprobación
	 * 		+ Comprobar que el mensaje ya no está en la carpeta original
	 * 		+ Comprobar que la carpeta original tiene el mismo numero de mensajes que antes menos 1
	 * 		+ Comprobar que el mensaje ahora aparece en SpamBox
	 * 		+ Comprobar que SpamBox ahora tiene el mismo numero de mensajes que antes más 1
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testFlagMessageAsSpam() {
		// Declare variables
		Actor user;
		Collection<Folder> actorFolders;
		Folder folder;
		Folder actorSpamFolder;
		Collection<Message> folderMessages;
		Message message;
		Integer folderMessagesSize;
		Integer spamMessagesSize;
		
		Collection<Folder> newActorFolders;
		Folder originalFolder;
		Collection<Message> originalFolderMessages;
		Folder spamFolder;
		Collection<Message> spamFolderMessages;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		
		folder = null;
		actorSpamFolder = null;
		
		for(Folder f: actorFolders){
			if(f.getName().equals("InBox")){
				folder = f;
			}
			if(f.getName().equals("SpamBox")){
				actorSpamFolder = f;
			}
		}
		
		Assert.notNull(folder, "No hay ninguna carpeta con la que se pretender testear.");
		
		folderMessages = messageService.findAllByFolder(folder);
		spamFolderMessages = messageService.findAllByFolder(actorSpamFolder);
		
		folderMessagesSize = folderMessages.size();
		spamMessagesSize = spamFolderMessages.size();
		
		message = null;
		
		for(Message m: folderMessages){
			message = m;
			break;
		}
		
		Assert.notNull(message, "No hay ningun mensaje con el que se pretender testear.");
		
		messageService.flagAsSpam(message.getId());
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		
		originalFolder = null;
		spamFolder = null;
		
		for(Folder f: newActorFolders){
			if(f.getName().equals("InBox")){
				originalFolder = f;
			}
			if(f.getName().equals("SpamBox")){
				spamFolder = f;
			}
		}
		
		Assert.notNull(spamFolder, "No hay ninguna carpeta de Spam con la que se pretender testear.");
		
		originalFolderMessages = messageService.findAllByFolder(originalFolder);
		
		spamFolderMessages = messageService.findAllByFolder(spamFolder);
		
		Assert.isTrue(!originalFolderMessages.contains(message), "El mensaje todavía está en la carpeta original.");
		
		Assert.isTrue(originalFolderMessages.size() == folderMessagesSize - 1, "El numero de mensajes de la carpeta original no es el mismo que había antes menos 1.");
		
		Assert.isTrue(spamFolderMessages.contains(message), "El mensaje no está en la carpeta de spam.");
		
		Assert.isTrue(spamFolderMessages.size() == spamMessagesSize + 1, "El numero de mensajes de la carpeta de spam no es el mismo que había antes mas 1.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Marcar un mensaje como Spam que está en Spam (Test planteado como negativo pero implementado como positivo al no saltar una excepción si no estar controlado de manera natural en el servicio para que no interrumpa la ejecución de la aplicación)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Acceder a su carpeta de Spam
	 * 		+ Marcar uno de sus mensajes como Spam
	 * 		- Comprobación
	 * 		+ Comprobar que el mensaje no desparece de la carpeta de Spam ni tiene comportamientos extraños, debe permanecer en Spam como si no hubiera pasado nada.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testFlagSpamMessageAsSpam() {
		// Declare variables
		Actor user;
		Collection<Folder> actorFolders;
//		Folder folder;
		Folder actorSpamFolder;
//		Collection<Message> folderMessages;
		Message message;
//		Integer folderMessagesSize;
		Integer spamMessagesSize;
		
		Collection<Folder> newActorFolders;
//		Folder originalFolder;
//		Collection<Message> originalFolderMessages;
		Folder spamFolder;
		Collection<Message> spamFolderMessages;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		actorFolders = folderService.findAllByActor();
		
//		folder = null;
		actorSpamFolder = null;
		
		for(Folder f: actorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
			if(f.getName().equals("SpamBox")){
				actorSpamFolder = f;
			}
		}
		
//		Assert.notNull(folder, "No hay ninguna carpeta con la que se pretender testear.");
		Assert.notNull(actorSpamFolder, "No hay ninguna carpeta con la que se pretender testear.");
		
//		folderMessages = messageService.findAllByFolder(folder);
		spamFolderMessages = messageService.findAllByFolder(actorSpamFolder);
		
//		folderMessagesSize = folderMessages.size();
		spamMessagesSize = spamFolderMessages.size();
		
		message = null;
		
		for(Message m: spamFolderMessages){
			message = m;
			break;
		}
		
		Assert.notNull(message, "No hay ningun mensaje con el que se pretende testear.");
		
		messageService.flagAsSpam(message.getId());
		
		// Checks results
		newActorFolders = folderService.findAllByActor();
		
//		originalFolder = null;
		spamFolder = null;
		
		for(Folder f: newActorFolders){
//			if(f.getName().equals("InBox")){
//				originalFolder = f;
//			}
			if(f.getName().equals("SpamBox")){
				spamFolder = f;
			}
		}
		
		Assert.notNull(spamFolder, "No hay ninguna carpeta de Spam con la que se pretender testear.");
		
//		originalFolderMessages = messageService.findAllByFolder(originalFolder);
		
		spamFolderMessages = messageService.findAllByFolder(spamFolder);
		
//		Assert.isTrue(!originalFolderMessages.contains(message), "El mensaje todavía está en la carpeta original.");
		
//		Assert.isTrue(originalFolderMessages.size() == folderMessagesSize - 1, "El numero de mensajes de la carpeta original no es el mismo que había antes menos 1.");
		
		Assert.isTrue(spamFolderMessages.contains(message), "El mensaje ha desaparecido de la carpeta de spam.");
		
		Assert.isTrue(spamFolderMessages.size() == spamMessagesSize, "El numero de mensajes de la carpeta de spam no es el mismo que había antes.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Marcar un mensaje de otro User como Spam
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Marcar un mensajes de otro User como Spam
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testFlagOtherUserMessageAsSpam() {
		// Declare variables
		Actor user;
		Collection<Folder> actorFolders;
//		Folder folder;
//		Folder actorSpamFolder;
//		Collection<Message> folderMessages;
		Message message;
		Boolean isMine; // Is the message in any folder of the user1?
//		Integer folderMessagesSize;
//		Integer spamMessagesSize;
//		
//		Collection<Folder> newActorFolders;
//		Folder originalFolder;
//		Collection<Message> originalFolderMessages;
//		Folder spamFolder;
//		Collection<Message> spamFolderMessages;
		
		// Load objects to test
		authenticate("user1");
		user = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		unauthenticate();
		authenticate("user2");
		actorFolders = folderService.findAllByActor();
		
//		folder = null;
////		actorSpamFolder = null;
//		
//		for(Folder f: actorFolders){
//			if(f.getName().equals("InBox")){
//				folder = f;
//			}
////			if(f.getName().equals("SpamBox")){
////				actorSpamFolder = f;
////			}
//		}
		
//		Assert.notNull(folder, "No hay ninguna carpeta con la que se pretender testear.");
		
//		folderMessages = messageService.findAllByFolder(folder);
//		spamFolderMessages = messageService.findAllByFolder(actorSpamFolder);
		
//		folderMessagesSize = folderMessages.size();
//		spamMessagesSize = spamFolderMessages.size();
		
		message = null;
		
		for(Folder f: actorFolders){
			for(Message m: messageService.findAllByFolder(f)){
				isMine = false;
				
				if(m.getSender() == user){
					isMine = true;
				}else{
					for(Actor a: m.getRecipients()){
						if(a == user){
							isMine = true;
						}
					}
				}
				
				if(!isMine){
					message = m;
					break;
				}
			}
		}
		
		Assert.notNull(message, "No hay ningun mensaje con las condiciones que se necesitan para testear.");
		
		unauthenticate();
		authenticate("user1");
		
		messageService.flagAsSpam(message.getId());
		
		// Checks results
//		newActorFolders = folderService.findAllByActor();
//		
//		originalFolder = null;
//		spamFolder = null;
//		
//		for(Folder f: newActorFolders){
//			if(f.getName().equals("InBox")){
//				originalFolder = f;
//			}
//			if(f.getName().equals("SpamBox")){
//				spamFolder = f;
//			}
//		}
//		
//		Assert.notNull(spamFolder, "No hay ninguna carpeta de Spam con la que se pretender testear.");
//		
//		originalFolderMessages = messageService.findAllByFolder(originalFolder);
//		
//		spamFolderMessages = messageService.findAllByFolder(spamFolder);
//		
//		Assert.isTrue(!originalFolderMessages.contains(message), "El mensaje todavía está en la carpeta original.");
//		
//		Assert.isTrue(originalFolderMessages.size() == folderMessagesSize - 1, "El numero de mensajes de la carpeta original no es el mismo que había antes menos 1.");
//		
//		Assert.isTrue(spamFolderMessages.contains(message), "El mensaje no está en la carpeta de spam.");
//		
//		Assert.isTrue(spamFolderMessages.size() == spamMessagesSize + 1, "El numero de mensajes de la carpeta de spam no es el mismo que había antes mas 1.");
//		
		unauthenticate();

	}
	
}
