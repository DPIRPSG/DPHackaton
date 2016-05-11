package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import security.UserAccountService;
import utilities.AbstractTest;
import domain.Actor;
import domain.Administrator;
import domain.Barter;
import domain.Match;
import domain.SocialIdentity;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private UserService userService;
	
	// Other services needed -----------------------
	
	@Autowired
	private UserAccountService userAccountService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private SocialIdentityService socialIdentityService;
	
	@Autowired
	private BarterService barterService;
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private AdministratorService administratorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Barter - Level C - 9.1
	 * Register to the system as a user.
	 */
	
	/**
	 * Positive test case: Registrarse como User
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Rellenar los campos
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Entrar al sistema con privilegios de administrador
	 * 		+ Comprobar que existe ese nuevo usuario entre los usuarios registrados
	 * 		+ Cerrar su sesión
	 * 		+ Comprobar que puede loguearse con el nuevo usuario creado
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testNewUser() {
		// Declare variables
		User user;
		UserAccount userAccount;
		User userRegistered;
		Actor authenticatedUser;
		
		// Load objects to test
		
		
		// Checks basic requirements
		
		
		// Execution of test
		user = userService.create();
		
		user.setName("Nuevo");
		user.setSurname("User");
		user.setPhone("123456789");

		userAccount = userAccountService.create("USER");
		
		userAccount.setUsername("nuevoUser");
		userAccount.setPassword("nuevoUser");
		
		user.setUserAccount(userAccount);
		
		userRegistered = userService.saveFromOtherService(user);
		
		// Checks results
		authenticate("admin");
		Assert.isTrue(userService.findAll().contains(userRegistered), "El user nuevo registrado no se encuentra entre los users registrados en el sistema."); // First check
		unauthenticate();
		
		authenticate("nuevoUser");
		
		authenticatedUser = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedUser, "No se ha podido loguear al user que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrarse como User con contraseña a null
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Rellenar los campos y la contraseña a null
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testNewUserNullPassword() {
		// Declare variables
		User user;
		UserAccount userAccount;
//		User userRegistered;
//		Actor authenticatedUser;
		
		// Load objects to test
		
		
		// Checks basic requirements
		
		
		// Execution of test
		user = userService.create();
		
		user.setName("Nuevo");
		user.setSurname("User");
		user.setPhone("123456789");

		userAccount = userAccountService.create("USER");
		
		userAccount.setUsername("nuevoUser");
		userAccount.setPassword(null);
		
		user.setUserAccount(userAccount);
		
		userService.saveFromOtherService(user);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(userService.findAll().contains(userRegistered), "El user nuevo registrado no se encuentra entre los users registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("user1");
//		
//		authenticatedUser = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedUser, "No se ha podido loguear al user que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrarse como User estando identificado en el sistema
	 * 		- Acción
	 * 		+ Entrar el registro como user
	 * 		+ Rellenar los campos y la contraseña a null
	 * 		+ Presionar en registrarse
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testNewUserAsUser() {
		// Declare variables
		User user;
		UserAccount userAccount;
//		User userRegistered;
//		Actor authenticatedUser;
		
		// Load objects to test
		authenticate("user1");
		
		// Checks basic requirements
		
		
		// Execution of test
		user = userService.create();
		
		user.setName("Nuevo");
		user.setSurname("User");
		user.setPhone("123456789");

		userAccount = userAccountService.create("USER");
		
		userAccount.setUsername("nuevoUser");
		userAccount.setPassword("nuevoUser");
		
		user.setUserAccount(userAccount);
		
		userService.saveFromOtherService(user);
		
		// Checks results
//		authenticate("admin");
//		Assert.isTrue(userService.findAll().contains(userRegistered), "El user nuevo registrado no se encuentra entre los users registrados en el sistema."); // First check
//		unauthenticate();
//		
//		authenticate("user1");
//		
//		authenticatedUser = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedUser, "No se ha podido loguear al user que se acaba de registrar."); // Second check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level C - 9.4
	 * List and see the profile of the users who have registered to the system, which consists
	 * of the following data: their name and surname, their social identities (if any),
	 * the list of barters that they have registered, and the list of matches in which they are
	 * involved.
	 */
	
	/**
	 * Positive test case: Listar el perfil de un User
	 * 		- Acción
	 * 		+ Entrar el registro como anónimo
	 * 		+ Listar todos los usuarios del sistema
	 * 		+ Listar el perfil de un usuario
	 * 		- Comprobación
	 * 		+ Comprobar que el número de usuarios del sistema traidos es el esperado.
	 * 		+ Comprobar que el nombre y apellidos del usuario son los esperados
	 * 		+ Comprobar que el número de SocialIdentities, Barters y Match, son los esperados.
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testListProfile() {
		// Declare variables
		Collection<User> users;
		User userToList;
		Collection<SocialIdentity> socialIdentities;
		Collection<Barter> barters;
		Collection<Match> matches;
		
		// Load objects to test
		
		
		// Checks basic requirements
		
		
		// Execution of test
		authenticate("user2");
		
		users = userService.findAll();
		
		userToList = null;
		
		for(User u: users){
			if(u.getUserAccount().getUsername().equals("user1")){
				userToList = u;
				break;
			}
		}
		
		Assert.notNull(userToList, "No existe el User que se pretende testear.");
		
		socialIdentities = socialIdentityService.findByUserId(userToList.getId());
		
		barters = barterService.findByUserNotCancelled(userToList.getId());
		
		matches = matchService.findAllUserInvolves(userToList.getId());
		
		// Checks results
		Assert.isTrue(users.size() == 7, "El numero de usuarios del sistema traidos no es el esperado.");
		
		Assert.isTrue(userToList.getName().equals("Manolo"), "El nombre del usuario no es el esperado.");
		
		Assert.isTrue(userToList.getSurname().equals("Lopez"), "El apellido del usuario no es el esperado.");
		
		Assert.isTrue(socialIdentities.size() == 6, "El numero de socialIdentities del usuario no es el esperado.");
		
		Assert.isTrue(barters.size() == 3, "El numero de barters del usuario no es el esperado.");
		
		Assert.isTrue(matches.size() == 3, "El numero de matches del usuario no es el esperado.");
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level B - 3.1
	 * Follow another user.
	 */
	
	/**
	 * Acme-Barter - Level B - 3.2
	 * List the users that he or she follows.
	 */
	
	/**
	 * Acme-Barter - Level B - 3.3
	 * List the users who follow him or her.
	 */
	
	/**
	 * Positive test case: Seguir a otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Listar los usuarios del sistema
	 * 		+ Seguir a uno de ellos (Comprobar que no lo siga en ese momento)
	 * 		- Comprobación
	 * 		+ Comprobar que el otro usuario aparece en su lista de seguidos
	 * 		+ Comprobar que su lista de seguidos contiene 1 usuario más que antes
	 * 		+ Cerrar su sesión
	 * 		+ Autenticarse en el sistema como el otro user al que ha seguido
	 * 		+ Comprobar que el usuario que le siguió aparece ahora en su lista de seguidores
	 * 		+ Comprobar que la lista de seguidores del usuario que le siguió tiene 1 usuario más
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testFollowOtherUser() {
		// Declare variables
		User user;
		User userToFollow;
		Collection<User> users;
		Integer userFollowedSize;
		Integer newUserFollowedSize;
		Integer userToFollowFollowersSize;
		Integer newUserToFollowFollowersSize;
		
		// Load objects to test
		authenticate("user2");
		user = userService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		users = userService.findAll();
		
		userToFollow = null;
		
		for(User u: users){
			if(u.getUserAccount().getUsername().equals("user4")){
				userToFollow = u;
			}
		}
		
		Assert.notNull(userToFollow, "No existe el usuario con el que se pretende testear.");
		
		userFollowedSize = userService.getFollowed().size();
		unauthenticate();
		
		authenticate("user4");
		userToFollowFollowersSize = userService.getFollowers().size();
		unauthenticate();
		
		authenticate("user2");
		Assert.isTrue(!userService.getFollowed().contains(userToFollow), "El usuario ya sigue al otro usuario al que se pretende seguir.");
		
		userService.followOrUnfollowById(userToFollow.getId());
		
		
		// Checks results
		newUserFollowedSize = userService.getFollowed().size();
		Assert.isTrue(user.getFollowed().contains(userToFollow), "El usuario no sigue al usuario que ha intentado seguir.");
		Assert.isTrue(userService.getFollowed().contains(userToFollow), "El usuario no sigue al usuario que ha intentado seguir.");
		
		Assert.isTrue(userFollowedSize + 1 == newUserFollowedSize, "El numero de seguidos por el usuario no es el mismo que antes mas 1.");
		
		unauthenticate();
		
		authenticate("user4");
		
		newUserToFollowFollowersSize = userService.getFollowers().size();
		Assert.isTrue(userService.getFollowers().contains(user), "El usuario seguido no contiene al usuario que le pretendía seguir entre sus seguidos.");
		
		Assert.isTrue(userToFollowFollowersSize + 1 == newUserToFollowFollowersSize, "El numero de seguidores del usuario seguido no es el mismo que antes mas 1.");
		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Dejar de seguir a otro usuario
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Listar los usuarios del sistema
	 * 		+ Dejar de seguir a uno de ellos (Comprobar que lo siga en ese momento)
	 * 		- Comprobación
	 * 		+ Comprobar que el otro usuario ya no aparece en su lista de seguidos
	 * 		+ Cerrar su sesión
	 * 		+ Autenticarse en el sistema como el otro user al que ha dejado de seguir
	 * 		+ Comprobar que el usuario que le siguió ya no aparece ahora en su lista de seguidores
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testUnfollowOtherUser() {
		// Declare variables
		User user;
		User userToUnfollow;
		Collection<User> users;
		
		// Load objects to test
		authenticate("user1");
		user = userService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		users = userService.findAll();
		
		userToUnfollow = null;
		
		for(User u: users){
			if(u.getUserAccount().getUsername().equals("user2")){
				userToUnfollow = u;
			}
		}
		
		Assert.notNull(userToUnfollow, "No existe el usuario con el que se pretende testear.");
		
		Assert.isTrue(userService.getFollowed().contains(userToUnfollow), "El usuario no sigue al otro usuario al que se pretende dejar de seguir.");
		
		userService.followOrUnfollowById(userToUnfollow.getId());
		
		
		// Checks results
		Assert.isTrue(!user.getFollowed().contains(userToUnfollow), "El usuario ya sigue al usuario que ha intentado dejar de seguir.");
		Assert.isTrue(!userService.getFollowed().contains(userToUnfollow), "El usuario ya sigue al usuario que ha intentado dejar de seguir.");
		
		unauthenticate();
		
		authenticate("user2");
		
		Assert.isTrue(!userService.getFollowers().contains(user), "El usuario seguido contiene al usuario que le pretendía dejar de seguir entre sus seguidos.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Seguir/Dejar de seguir a ti mismo
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Listar los usuarios del sistema
	 * 		+ Intentar seguir/dejar de seguir al mismo usuario
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testFollowOrUnfollowYourself() {
		// Declare variables
		User user;
		
		// Load objects to test
		authenticate("user1");
		user = userService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		userService.followOrUnfollowById(user.getId());
		
		// Checks results
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Seguir a un admin (Otro actor que no sea de tipo User)
	 * 		- Acción
	 * 		+ Autenticarse en el sistema
	 * 		+ Intentar seguir a un administrador
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testFollowAnAdmin() {
		// Declare variables
		User user;
		Administrator admin;
		
		// Load objects to test
		authenticate("admin");
		admin = administratorService.findByPrincipal();
		unauthenticate();
		
		authenticate("user1");
		user = userService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(user, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		
		userService.followOrUnfollowById(admin.getId());
		
		// Checks results
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level C - 12.5.1
	 * The total number of users who have registered to the system.
	 */
	@Test 
	public void testTotalUsers() {
		// Declare variables
		int totalUsersInTest;
		int result;
		
		// Load objects to test
		authenticate("admin");
		totalUsersInTest = userService.findAll().size();
		
		// Checks basic requirements
				
		// Execution of test
		
		result = userService.getTotalNumberOfUsersRegistered();
		Assert.isTrue(result == totalUsersInTest);
				
		// Checks results	
	}
	
	/**
	 * Acme-Barter - Level C - 12.5.5
	 * The users who have not created any barter during the last month.
	 */
	@Test 
	public void testUsersNotCreateRecentBarter() {
		// Declare variables
		Collection<User> inTest;
		Collection<User> result;
		Calendar limitCalendar;
		Date limitDate;
		
		// Load objects to test
		authenticate("admin");
		
		inTest = userService.findAll();
		limitCalendar = Calendar.getInstance();
		limitCalendar.add(Calendar.MONTH, -1);
		limitDate = limitCalendar.getTime();
		
		for(Barter b:barterService.findAll()){
			boolean isRecent;
			
			isRecent = b.getRegisterMoment().after(limitDate);
			
			if(inTest.contains(b.getUser()) && isRecent){
				inTest.remove(b.getUser());
			}
		}
		
		// Checks basic requirements

		// Execution of test
		result = userService.getUsersWithNoBarterThisMonth();
		Assert.isTrue(
				result.containsAll(inTest) && inTest.containsAll(result),
				"El test devuelve " + result.size() + " " + result.toString()
						+ " pero debían ser " + inTest.size() + " "
						+ inTest.toString());
		
				// En la variable inTest están los usuarios que debería devolver la query
		
		
		// Checks results	
	}
	
	/**
	 * Acme-Barter - Level B - 4.2.3
	 * The users who have registered more barters.
	 */
	@Test 
	public void testUsersMoreBarters() {
		// Declare variables
		Collection<User> inTest;
		Collection<User> result;
		Map<User, Integer> userBarters;
		int maxBarters;
		
		// Load objects to test
		authenticate("admin");
		
		inTest = userService.findAll();
		
		userBarters = new HashMap<User, Integer>();
		maxBarters = 0;
		
		for(Barter b:barterService.findAll()){
			int value = 1;
			if(userBarters.containsKey(b.getUser()))
				value += userBarters.get(b.getUser());

			userBarters.put(b.getUser(), value);
			
			if(value > maxBarters){
				inTest.clear();
				maxBarters = value;
			}
			if(value >= maxBarters)
				inTest.add(b.getUser());
		}
		
		// Checks basic requirements

		// Execution of test
		result = userService.getUsersWithMoreBarters();
		
		Assert.isTrue(
				result.containsAll(inTest) && inTest.containsAll(result),
				"El test devuelve " + result.size() + " " + result.toString()
						+ " pero debían ser " + inTest.size() + " "
						+ inTest.toString());
		
				// En la variable inTest están los usuarios que debería devolver la query
		
		
		// Checks results	
	}
	
	/**
	 * Acme-Barter - Level B - 4.2.4
	 * The users who have more cancelled barters.
	 */
	@Test 
	public void testUsersMoreCancelledBarters() {
		// Declare variables
		Collection<User> inTest;
		Collection<User> result;
		Map<User, Integer> userBarters;
		int maxBarters;
		
		// Load objects to test
		authenticate("admin");
		
		inTest = userService.findAll();
		
		userBarters = new HashMap<User, Integer>();
		maxBarters = 0;

		for (Barter b : barterService.findAll()) {
			if (b.isCancelled()) {
				int value = 1;
				if (userBarters.containsKey(b.getUser()))
					value += userBarters.get(b.getUser());

				userBarters.put(b.getUser(), value);

				if (value > maxBarters) {
					inTest.clear();
					maxBarters = value;
				}
				if (value >= maxBarters)
					inTest.add(b.getUser());
			}
		}

		// Checks basic requirements

		// Execution of test
		result = userService.getUsersWithMoreBartersCancelled();
		
		Assert.isTrue(
				result.containsAll(inTest) && inTest.containsAll(result),
				"El test devuelve " + result.size() + " " + result.toString()
						+ " pero debían ser " + inTest.size() + " "
						+ inTest.toString());
		
				// En la variable inTest están los usuarios que debería devolver la query
		
		
		// Checks results	
	}
	
	/**
	 * Acme-Barter - Level B - 4.2.5
	 * The users who have more matches.
	 */
	@Test 
	public void testUsersMoreMatches() {
		// Declare variables
		Collection<User> inTest;
		Collection<User> result;
		Map<User, Integer> userBarters;
		int maxBarters;
		
		// Load objects to test
		authenticate("admin");
		
		inTest = userService.findAll();
		
		userBarters = new HashMap<User, Integer>();
		maxBarters = 0;
		
		for (Match b : matchService.findAll()) {
			// createMatch
			int value = 1;
			if (userBarters.containsKey(b.getCreatorBarter().getUser()))
				value += userBarters.get(b.getCreatorBarter().getUser());

			userBarters.put(b.getCreatorBarter().getUser(), value);

			if (value > maxBarters) {
				inTest.clear();
				maxBarters = value;
			}
			if (value >= maxBarters)
				inTest.add(b.getCreatorBarter().getUser());

			// receivedMatch
			if (b.getReceiverBarter() != null) {
				value = 1;
				if (userBarters.containsKey(b.getReceiverBarter().getUser()))
					value += userBarters.get(b.getReceiverBarter().getUser());

				userBarters.put(b.getReceiverBarter().getUser(), value);

				if (value > maxBarters) {
					inTest.clear();
					maxBarters = value;
				}
				if (value >= maxBarters)
					inTest.add(b.getReceiverBarter().getUser());

			}

		}
		
		// Checks basic requirements

		// Execution of test
		result = userService.getUsersWithMoreMatches();
		
		Assert.isTrue(
				result.containsAll(inTest) && inTest.containsAll(result),
				"El test devuelve " + result.size() + " " + result.toString()
						+ " pero debían ser " + inTest.size() + " "
						+ inTest.toString());
		
				// En la variable inTest están los usuarios que debería devolver la query
		
		
		// Checks results	
	}
	
	/**
	 * Acme-Barter - Level A - 3.2.2
	 * The users who have got more matches audited.

	 */
	@Test 
	public void testUsersMoreMatchesAudited() {
		// Declare variables
		Collection<User> inTest;
		Collection<User> result;
		Map<User, Integer> userBarters;
		int maxBarters;
		
		// Load objects to test
		authenticate("admin");
		
		inTest = new ArrayList<User>();
		
		userBarters = new HashMap<User, Integer>();
		maxBarters = 0;
		
		for (Match b : matchService.findAll()) {
			if (b.getAuditor() != null) {
				// createMatch
				int value = 1;
				if (userBarters.containsKey(b.getCreatorBarter().getUser()))
					value += userBarters.get(b.getCreatorBarter().getUser());

				userBarters.put(b.getCreatorBarter().getUser(), value);

				if (value > maxBarters) {
					inTest.clear();
					maxBarters = value;
				}
				if (value >= maxBarters)
					inTest.add(b.getCreatorBarter().getUser());
				
				//receivedMatch
				if(b.getReceiverBarter() != null){
					value = 1;
					if (userBarters.containsKey(b.getReceiverBarter().getUser()))
						value += userBarters.get(b.getReceiverBarter().getUser());

					userBarters.put(b.getReceiverBarter().getUser(), value);

					if (value > maxBarters) {
						inTest.clear();
						maxBarters = value;
					}
					if (value >= maxBarters)
						inTest.add(b.getReceiverBarter().getUser());
									
				}
			}
		}
		
		// Checks basic requirements

		// Execution of test
		
		result = userService.getUsersWithMoreMatchesAudited();
		
		Assert.isTrue(
				result.containsAll(inTest) && inTest.containsAll(result),
				"El test devuelve " + result.size() + " " + result.toString()
						+ " pero debían ser " + inTest.size() + " "
						+ inTest.toString());
		
				// En la variable inTest están los usuarios que debería devolver la query
		
		
		// Checks results	
	}
	
	/**
	 * Acme-Barter - 2.0 - Level C - 4.2.4
	 *  The users who have created more complaints than the average.
	 */
	@Test 
	public void testUsersMoreComplaintsThanAverage() {
		// Declare variables
		Collection<User> codeResult;
		Collection<User> testCalculate;
		double average;
		
		// Load objects to test
		authenticate("admin");
		
		average = complaintService.findAll().size() / userService.findAll().size();
		
		testCalculate = new ArrayList<User>();
		
		for (User u:userService.findAll()){
			double calculate;
			calculate = complaintService.findByUser(u).size();
			if(calculate >= average){
				testCalculate.add(u);
			}
		}
		
		// Checks basic requirements

		// Execution of test
		
		codeResult = userService.getUsersWhoHaveCreatedMoreComplaintsThatAverage();
		
				// En la variable inTest están los usuarios que debería devolver la query
		
		// Checks results	
		
		Assert.isTrue(testCalculate.size() == codeResult.size(), "Se esperaban '" + testCalculate.size() + "' pero han sido '" + codeResult.size() +"'");
		Assert.isTrue(codeResult.containsAll(testCalculate), "El método no devuelve lo esperado");
		Assert.isTrue(testCalculate.containsAll(codeResult), "El método no devuelve lo esperado");
	}
	
}
