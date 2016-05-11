package services;

import java.util.Collection;
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
import domain.Auditor;
import domain.Match;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml",
	"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class AuditorServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	
	@Autowired
	private AuditorService auditorService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private MatchService matchService;
	
	@Autowired
	private UserAccountService userAccountService;
	
//	@Autowired
//	private ServiceService serviceService;
	
	// Test ---------------------------------------
	
//	/**
//	 * Description: A user who is not authenticated must be able to search for a auditor using a single keyword that must appear in his or name, surname, or curriculum.
//	 * Precondition: The given key word is found in a name.
//	 * Return: TRUE
//	 * Postcondition: All auditor that contains the given key word in its name, surname or curriculum are shown.
//	 */
//	@Test
//	public void findAllAuditorsByGivenKeyword1(){
//		
//		Collection<Auditor> all;
//		
//		all = auditorService.findBySingleKeyword("Pablo");
//		
//		Assert.isTrue(all.size() == 1);
//		
//		auditorService.flush();
//		
//	}
//	
//	/**
//	 * Description: A user who is not authenticated must be able to search for a auditor using a single keyword that must appear in his or name, surname, or curriculum.
//	 * Precondition: The given key word is found in a surname
//	 * Return: TRUE
//	 * Postcondition: All auditor that contains the given key word in its name, surname or curriculum are shown.
//	 */
//	@Test
//	public void findAllAuditorsByGivenKeyword2(){
//		
//		Collection<Auditor> all;
//		
//		all = auditorService.findBySingleKeyword("Gil");
//		
//		Assert.isTrue(all.size() == 1);
//		
//		auditorService.flush();
//	}
//	
//	/**
//	 * Description: A user who is not authenticated must be able to search for a auditor using a single keyword that must appear in his or name, surname, or curriculum.
//	 * Precondition: The given key word is found in a surname
//	 * Return: TRUE
//	 * Postcondition: All auditor that contains the given key word in its name, surname or curriculum are shown.
//	 */
//	@Test
//	public void findAllAuditorsByGivenKeyword3(){
//		
//		Collection<Auditor> all;
//		
//		all = auditorService.findBySingleKeyword("Mata");
//				
//		Assert.isTrue(all.size() == 1);
//		
//		auditorService.flush();
//	}
//	
//	/**
//	 * Description: A user who is not authenticated must be able to search for a auditor using a single keyword that must appear in his or name, surname, or curriculum.
//	 * Precondition: The given key word is found in a curriculum.
//	 * Return: TRUE
//	 * Postcondition: All auditor that contains the given key word in its name, surname or curriculum are shown.
//	 */
//	@Test
//	public void findAllAuditorsByGivenKeyword4(){
//		
//		Collection<Auditor> all;
//		
//		all = auditorService.findBySingleKeyword("Hola");
//				
//		Assert.isTrue(all.size() == 5);
//		
//		auditorService.flush();
//	}
//	
//	/**
//	 * Description: A user who is not authenticated must be able to search for a auditor using a single keyword that must appear in his or name, surname, or curriculum.
//	 * Precondition: The given key word is found in a curriculum.
//	 * Return: TRUE
//	 * Postcondition: All auditor that contains the given key word in its name, surname or curriculum are shown.
//	 */
//	@Test
//	public void findAllAuditorsByGivenKeyword5(){
//		
//		Collection<Auditor> all;
//		
//		all = auditorService.findBySingleKeyword("Hololens");
//				
//		Assert.isTrue(all.size() == 0);
//		
//		auditorService.flush();
//	}
	
	/**
	 * Acme-Barter - Level A - 3.1
	 * Register an auditor to the system.
	 */
	
	/**
	 * Positive test case: Registrar un nuevo Auditor
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Auditor
	 * 		- Comprobación
	 * 		+ Listar los auditors
	 * 		+ Comprobar que hay 1 más de los que había
	 * 		+ Comprobar que el nuevo auditor se encuentra entre ellos
	 * 		+ Cerrar su sesión
	 * 		+ Comprobar que puedes loguearte con el nuevo Auditor
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testRegisterAuditor() {
		// Declare variables
		Actor admin;
		Auditor auditor;
		UserAccount userAccount;
		Auditor auditorRegistered;
		Integer numberOfAuditors;
		Integer newNumberOfAuditors;
		Collection<Auditor> auditors;
		Actor authenticatedAuditor;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		numberOfAuditors = auditorService.findAll().size();
		
		auditor = auditorService.create();
		
		auditor.setName("Nuevo");
		auditor.setSurname("Auditor");
		auditor.setPhone("123456789");

		userAccount = userAccountService.create("AUDITOR");
		
		userAccount.setUsername("nuevoAuditor");
		userAccount.setPassword("nuevoAuditor");
		
		auditor.setUserAccount(userAccount);
		
		auditorRegistered = auditorService.save(auditor);
		
		// Checks results
		auditors = auditorService.findAll();
		newNumberOfAuditors = auditors.size();
		
		Assert.isTrue(numberOfAuditors + 1 == newNumberOfAuditors, "El numero de auditors tras el registro no es el mismo que antes + 1"); // First check
		
		Assert.isTrue(auditors.contains(auditorRegistered), "El Auditor registrado no se encuentra entre los auditors del sistema."); // Second check
		
		unauthenticate();
		
		authenticate("nuevoAuditor");
		
		authenticatedAuditor = actorService.findByPrincipal();
		
		Assert.notNull(authenticatedAuditor, "No se ha podido loguear al auditor que se acaba de registrar."); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Auditor sin estar autenticado
	 * 		- Acción
	 * 		+ Registrar un nuevo Auditor
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test
	public void testRegisterAuditorAsUnauthenticated() {
		// Declare variables
//		Actor admin;
		Auditor auditor;
		UserAccount userAccount;
//		Auditor auditorRegistered;
//		Integer numberOfAuditors;
//		Integer newNumberOfAuditors;
//		Collection<Auditor> auditors;
//		Actor authenticatedAuditor;
		
		// Load objects to test
//		authenticate("admin");
//		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
//		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfAuditors = auditorService.findAll().size();
		
		auditor = auditorService.create();
		
		auditor.setName("Nuevo");
		auditor.setSurname("Auditor");
		auditor.setPhone("123456789");

		userAccount = userAccountService.create("AUDITOR");
		
		userAccount.setUsername("nuevoAuditor");
		userAccount.setPassword("nuevoAuditor");
		
		auditor.setUserAccount(userAccount);
		
		auditorService.save(auditor);
		
		// Checks results
//		auditors = auditorService.findAll();
//		newNumberOfAuditors = auditors.size();
//		
//		Assert.isTrue(numberOfAuditors + 1 == newNumberOfAuditors, "El numero de auditors tras el registro no es el mismo que antes + 1"); // First check
//		
//		Assert.isTrue(auditors.contains(auditorRegistered), "El Auditor registrado no se encuentra entre los auditors del sistema."); // Second check
//		
//		unauthenticate();
//		
//		authenticate("nuevoAuditor");
//		
//		authenticatedAuditor = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedAuditor, "No se ha podido loguear al auditor que se acaba de registrar."); // Third check
//		
//		unauthenticate();

	}
	
	/**
	 * Negative test case: Registrar un nuevo Auditor con username a null
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como administrador
	 * 		+ Registrar un nuevo Auditor con username nulo
	 * 		- Comprobación
	 * 		+ Listar los auditors
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testRegisterAuditorNullUsername() {
		// Declare variables
		Actor admin;
		Auditor auditor;
		UserAccount userAccount;
//		Auditor auditorRegistered;
//		Integer numberOfAuditors;
//		Integer newNumberOfAuditors;
//		Collection<Auditor> auditors;
//		Actor authenticatedAuditor;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		numberOfAuditors = auditorService.findAll().size();
		
		auditor = auditorService.create();
		
		auditor.setName("Nuevo");
		auditor.setSurname("Auditor");
		auditor.setPhone("123456789");

		userAccount = userAccountService.create("AUDITOR");
		
		userAccount.setUsername(null);
		userAccount.setPassword("nuevoAuditor");
		
		auditor.setUserAccount(userAccount);
		
		auditorService.save(auditor);
		
		
		// Checks results
//		auditors = auditorService.findAll();
//		newNumberOfAuditors = auditors.size();
//		
//		Assert.isTrue(numberOfAuditors + 1 == newNumberOfAuditors, "El numero de auditors tras el registro no es el mismo que antes + 1"); // First check
//		
//		Assert.isTrue(auditors.contains(auditorRegistered), "El Auditor registrado no se encuentra entre los auditors del sistema."); // Second check
//		
//		unauthenticate();
//		
//		authenticate("nuevoAuditor");
//		
//		authenticatedAuditor = actorService.findByPrincipal();
//		
//		Assert.notNull(authenticatedAuditor, "No se ha podido loguear al auditor que se acaba de registrar."); // Third check
		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level A - 2.1
	 * Self-assign an existing match to audit it. Obviously, no match can be self-assigned to two different auditors.
	 */
	
	/**
	 * Positive test case: Asignarse un Match
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Auditor
	 * 		+ Listar los Matches
	 * 		+ Asignarse uno nuevo
	 * 		- Comprobación
	 * 		+ Listar sus Matches asignados
	 * 		+ Comprobar que el Match asignado aparece entre ellos
	 * 		+ Comprobar que el numero de Matchs que tiene asignados es el mismo que antes mas 1
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testSelfAssignAMatch() {
		// Declare variables
		Auditor auditor;
		Collection<Match> allMatches;
		Collection<Match> matchesAssigned;
		Collection<Match> newMatchesAssigned;
		Match matchToAssign;
		Integer matchesAssignedSize;
		Integer newMatchesAssignedSize;
		
		// Load objects to test
		authenticate("auditor1");
		auditor = auditorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(auditor, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		matchesAssigned = matchService.findAllByAuditor();
		matchesAssignedSize = matchesAssigned.size();
		
		allMatches = matchService.findAll();
		
		matchToAssign = null;
		
		for(Match m: allMatches){
			if(m.getAuditor() == null){
				matchToAssign = m;
				break;
			}
		}
		
		Assert.notNull(matchToAssign, "No hay ningún Match sin asignar para poder realizar el test.");
		
		matchService.selfAssignByAuditor(matchToAssign.getId());
		
		// Checks results
		newMatchesAssigned = matchService.findAllByAuditor();
		newMatchesAssignedSize = newMatchesAssigned.size();
		
		Assert.isTrue(newMatchesAssigned.contains(matchToAssign), "El auditor no contiene al Match entre sus Match asignados");
		
		Assert.isTrue(matchesAssignedSize + 1 == newMatchesAssignedSize, "El numero de Match asignados al Auditor no es el mismo que antes mas 1");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Asignarse un Match que esté asignado a otro
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Auditor
	 * 		+ Listar sus Matches asignados
	 * 		+ Asignarse uno nuevo que tenga asignado otro auditor
	 * 		- Comprobación
	 * 		+ Comrpobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testSelfAssignAMatchOfOtherAuditor() {
		// Declare variables
		Auditor auditor;
		Collection<Match> allMatches;
//		Collection<Match> matchesAssigned;
//		Collection<Match> newMatchesAssigned;
		Match matchToAssign;
//		Integer matchesAssignedSize;
//		Integer newMatchesAssignedSize;
		
		// Load objects to test
		authenticate("auditor1");
		auditor = auditorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(auditor, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		matchesAssigned = matchService.findAllByAuditor();
//		matchesAssignedSize = matchesAssigned.size();
		
		allMatches = matchService.findAll();
		
		matchToAssign = null;
		
		for(Match m: allMatches){
			if(m.getAuditor() != null && m.getAuditor() != auditor){
				matchToAssign = m;
				break;
			}
		}
		
		Assert.notNull(matchToAssign, "No hay ningún Match asignado a otro Auditor para poder realizar el test.");
		
		matchService.selfAssignByAuditor(matchToAssign.getId());
		
		// Checks results
//		newMatchesAssigned = matchService.findAllByAuditor();
//		newMatchesAssignedSize = newMatchesAssigned.size();
//		
//		Assert.isTrue(newMatchesAssigned.contains(matchToAssign), "El auditor no contiene al Match entre sus Match asignados");
//		
//		Assert.isTrue(matchesAssignedSize + 1 == newMatchesAssignedSize, "El numero de Match asignados al Auditor no es el mismo que antes mas 1");
//		
		unauthenticate();

	}

	/**
	 * Negative test case: Asignarse un Match que ya tengas asignado
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Auditor
	 * 		+ Asignarse uno nuevo que ya tenga asignado
	 * 		- Comprobación
	 * 		+ Comrpobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testSelfAssignAMatchAlreadyAssigned() {
		// Declare variables
		Auditor auditor;
		Collection<Match> allMatches;
//		Collection<Match> matchesAssigned;
//		Collection<Match> newMatchesAssigned;
		Match matchToAssign;
//		Integer matchesAssignedSize;
//		Integer newMatchesAssignedSize;
		
		// Load objects to test
		authenticate("auditor1");
		auditor = auditorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(auditor, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		matchesAssigned = matchService.findAllByAuditor();
//		matchesAssignedSize = matchesAssigned.size();
		
		allMatches = matchService.findAll();
		
		matchToAssign = null;
		
		for(Match m: allMatches){
			if(m.getAuditor() != null && m.getAuditor() == auditor){
				matchToAssign = m;
				break;
			}
		}
		
		Assert.notNull(matchToAssign, "No hay ningún Match asignado al Auditor para poder realizar el test.");
		
		matchService.selfAssignByAuditor(matchToAssign.getId());
		
		// Checks results
//		newMatchesAssigned = matchService.findAllByAuditor();
//		newMatchesAssignedSize = newMatchesAssigned.size();
//		
//		Assert.isTrue(newMatchesAssigned.contains(matchToAssign), "El auditor no contiene al Match entre sus Match asignados");
//		
//		Assert.isTrue(matchesAssignedSize + 1 == newMatchesAssignedSize, "El numero de Match asignados al Auditor no es el mismo que antes mas 1");
//		
		unauthenticate();

	}
	
	/**
	 * Acme-Barter - Level A - 2.2
	 * Write an auditing report regarding the matches that he or she's self-assigned.
	 */
	
	/**
	 * Positive test case: Escribir un report a un Match
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Auditor
	 * 		+ Listar sus Matches asignados
	 * 		+ Escribir un report para uno de ellos
	 * 		- Comprobación
	 * 		+ Listar el Match
	 * 		+ Comprobar que ahora tiene el report que ha escrito
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testWriteAReport() {
		// Declare variables
		Auditor auditor;
		Collection<Match> matchesAssigned;
		Match matchToReport;
		Match matchReported;
		
		// Load objects to test
		authenticate("auditor1");
		auditor = auditorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(auditor, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		matchesAssigned = matchService.findAllByAuditor();
		
		matchToReport = matchesAssigned.iterator().next();
		
		matchToReport.setReport("Nuevo Report");
		
		matchService.addReport(matchToReport);
		
		// Checks results
		matchReported = matchService.findOne(matchToReport.getId());
		
		Assert.isTrue(matchReported.getReport() == "Nuevo Report", "El Match no tiene escrito el report que ha intentado escribirle el Auditor.");
		
		unauthenticate();

	}
	
	/**
	 * Negative test case: Escribir un report a un Match que no esté asignado a ningún auditor
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Auditor
	 * 		+ Escribir un report para un Match que no tenga asignado ningún auditor
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testWriteAReportToMatchNotAssigned() {
		// Declare variables
		Auditor auditor;
		Collection<Match> allMatches;
		Match matchToReport;
//		Match matchReported;
		
		// Load objects to test
		authenticate("auditor1");
		auditor = auditorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(auditor, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		matchesAssigned = matchService.findAllByAuditor();
		
		allMatches = matchService.findAll();
		
		matchToReport = null;
		
		for(Match m: allMatches){
			if(m.getAuditor() == null){
				matchToReport = m;
			}
		}
		
		Assert.notNull(matchToReport, "No existe ningún match no asignado a un Auditor para poder realizar el test.");
		
//		matchToReport = matchesAssigned.iterator().next();
		
		matchToReport.setReport("Nuevo Report");
		
		matchService.addReport(matchToReport);
		
		// Checks results
//		matchReported = matchService.findOne(matchToReport.getId());
//		
//		Assert.isTrue(matchReported.getReport() == "Nuevo Report", "El Match no tiene escrito el report que ha intentado escribirle el Auditor.");
//		
		unauthenticate();

	}
	
	/**
	 * Positive test case: Escribir un report a un Match que tenga asignado otro Auditor
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Auditor
	 * 		+ Escribir un report para un Match que tenga asignado otro Auditor
	 * 		- Comprobación
	 * 		+ Comprobar que salta una excepción del tipo: IllegalArgumentException
	 * 		+ Cerrar su sesión
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true)
//	@Test 
	public void testWriteAReportToMatchAssignedToOtherAuditor() {
		// Declare variables
		Auditor auditor;
		Collection<Match> allMatches;
		Match matchToReport;
//		Match matchReported;
		
		// Load objects to test
		authenticate("auditor1");
		auditor = auditorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(auditor, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
//		matchesAssigned = matchService.findAllByAuditor();
		
		allMatches = matchService.findAll();
		
		matchToReport = null;
		
		for(Match m: allMatches){
			if(m.getAuditor() != null && m.getAuditor() != auditor){
				matchToReport = m;
			}
		}
		
		Assert.notNull(matchToReport, "No existe ningún match asignado a otro Auditor para poder realizar el test.");
		
//		matchToReport = matchesAssigned.iterator().next();
		
		matchToReport.setReport("Nuevo Report");
		
		matchService.addReport(matchToReport);
		
		// Checks results
//		matchReported = matchService.findOne(matchToReport.getId());
//		
//		Assert.isTrue(matchReported.getReport() == "Nuevo Report", "El Match no tiene escrito el report que ha intentado escribirle el Auditor.");
//		
		unauthenticate();

	}
	
	
	/**
	 * Acme-Barter - Level A - 3.2.1
	 * The auditors who have audited more matches.
	 */
	@Test 
	public void testAuditorMoreMatches() {
		// Declare variables
		Collection<Auditor> inTest;
		Collection<Auditor> result;
		Map<Auditor, Integer> auditorBarters;
		int maxBarters;
		
		// Load objects to test
		authenticate("admin");
		
		inTest = auditorService.findAll();
		
		auditorBarters = new HashMap<Auditor, Integer>();
		maxBarters = 0;
		
		for (Match b : matchService.findAll()) {
			if (b.getAuditor() != null) {
				// createMatch
				int value = 1;
				if (auditorBarters.containsKey(b.getAuditor()))
					value += auditorBarters.get(b.getAuditor());

				auditorBarters.put(b.getAuditor(), value);

				if (value > maxBarters) {
					inTest.clear();
					maxBarters = value;
				}
				if (value >= maxBarters)
					inTest.add(b.getAuditor());
				
			}
		}
		
		// Checks basic requirements

		// Execution of test
		result = auditorService.getAuditorsWithMoreMatches();
		Assert.isTrue(
				result.containsAll(inTest) && inTest.containsAll(result),
				"El test devuelve " + result.size() + " " + result.toString()
						+ " pero debían ser " + inTest.size() + " "
						+ inTest.toString());
				
				// En la variable inTest están los usuarios que debería devolver la query
		
		
		// Checks results	
	}
}
