package services;

import java.util.ArrayList;
import java.util.Collection;

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
import domain.Classification;
import domain.Club;
import domain.League;
import domain.Race;

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
	private RefereeService refereeService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ClubService clubService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - CORREGIR
	 * Rellenar la clasificación de los clubes en las carreras de las ligas que él dirige,
	 * con un procedimiento que lo haga automáticamente.
	 */
	
	/**
	 * Positive test case: Rellenar clasificación de los clubes de una liga que dirige.
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificación de los clubes de una liga
	 * 		- Comprobación
	 * 		+ Comprobar que la clasificación se ha actualizado
	 * 		+ Cerrar su sesión
	 */
	
	// CORREGIR
	@Test 
	public void testUpdateClassification() {
		// Declare variables
		Actor referee;
		Collection<League> leagues;
		League league;
		Club club;
		Collection<Classification> classifications;
//		Collection<Classification> newClassifications;
		Classification classification;
		int points;
//		Race race;
		int classificationId;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		leagues = leagueService.findAll();
		
		league = null;
		for(League l: leagues){
			if(l.getReferee() == referee){
				league = l; // Liga que dirige el referee1
				break;
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga para el referee1 para testear.");
		
		club = clubService.findAllByLeagueId(league.getId()).iterator().next(); // Un club que participa en esa liga
		
		classifications = club.getClassifications(); // Clasificaciones del club en todas las ligas que ha perticipados
		
		classification = null;
		for(Classification c: classifications){
			if(c.getRace().getLeague() == league){
				classification = c; // La clasificación del club en la liga seleccionada anteriormente
				break;
			}
		}
		
		classificationId = classification.getId();
		
		Assert.notNull(classification, "No hay ninguna clasificación como con la que se pretende testear.");
		
//		race = classification.getRace(); // Carrera de la clasifiación escogida
		
		points = classification.getPoints();
		
		classification.setPoints(999999999); // Asignamos puntos errónos
		
//		classifications = club.getClassifications();
		
		classificationService.calculateClassification(classification.getRace().getId()); // Recalculamos los puntos
		
//		club = clubService.findOne(club.getId());
		
		// Checks results
//		newClassifications = club.getClassifications();
//		
//		classification = null;
//		for(Classification c: newClassifications){
//			if(c.getRace().getLeague() == league){
//				classification = c;
//				break;
//			}
//		}
		
		classification = classificationService.findOne(classificationId);
		
		Assert.isTrue(classification.getPoints() == points, "Las clasificaciones no se han actualizado correctamente.");
		Assert.isTrue(classification.getPoints() != 999999999, "La clasificación del club mantiene los puntos editados a mano.");
		
		unauthenticate();

	}
	
	// CORREGIR : Primer test negativo
	/**
	 * Negative test case: Rellenar clasificación de los clubes de una liga que NO dirige.
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Referee
	 * 		+ Rellenar la clasificación de los clubes de una liga que NO dirija
	 * 		- Comprobación
	 * 		+ Comprobar que la clasificación se ha actualizado
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testUpdateClassificationOfLeagueNoOfReferee() {
		// Declare variables
		Actor referee;
		Collection<League> leagues;
		League league;
		Club club;
		Collection<Classification> classifications;
		Collection<Classification> newClassifications;
		Classification classification;
		int points;
		
		// Load objects to test
		authenticate("referee1");
		referee = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(referee, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		leagues = leagueService.findAll();
		
		league = null;
		for(League l: leagues){
			if(l.getReferee() != referee){
				league = l; // Liga que dirige el referee1
				break;
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga para el referee1 para testear.");
		
		club = clubService.findAllByLeagueId(league.getId()).iterator().next(); // Un club que participa en esa liga
		
		classifications = club.getClassifications(); // Clasificaciones del club en todas las ligas que ha perticipados
		
		classification = null;
		for(Classification c: classifications){
			if(c.getRace().getLeague() == league){
				classification = c; // La clasificación del club en la liga seleccionada anteriormente
				break;
			}
		}
		
		Assert.notNull(classification, "No hay ninguna clasificación como con la que se pretende testear.");
		
		points = classification.getPoints();
		
		classification.setPoints(999999999); // Asignamos puntos errónos
		
//		classifications = club.getClassifications();
		
		clubService.calculateRankingByLeague(league.getId()); // Recalculamos los puntos
		
//		club = clubService.findOne(club.getId());
		
		newClassifications = club.getClassifications();
		
		classification = null;
		for(Classification c: newClassifications){
			if(c.getRace().getLeague() == league){
				classification = c;
				break;
			}
		}
		
		// Checks results
		Assert.isTrue(classification.getPoints() == points, "Las clasificaciones no se han actualizado correctamente.");
		Assert.isTrue(classification.getPoints() != 999999999, "La clasificación del club mantiene los puntos editados a mano.");
		
		unauthenticate();

	}
	
	// CORREGIR : Segundo test negativo
	/**
	 * Negative test case: Rellenar clasificación de los clubes de una liga como Admin
	 * 		- Acción
	 * 		+ Autenticarse en el sistema como Admin
	 * 		+ Rellenar la clasificación de los clubes de una liga
	 * 		- Comprobación
	 * 		+ Comprobar que la clasificación se ha actualizado
	 * 		+ Cerrar su sesión
	 */
	
	@Test 
	public void testUpdateClassificationAsAdmin() {
		// Declare variables
		Actor admin;
		Collection<League> leagues;
		League league;
		Club club;
		Collection<Classification> classifications;
		Collection<Classification> newClassifications;
		Classification classification;
		int points;
		
		// Load objects to test
		authenticate("admin");
		admin = actorService.findByPrincipal();
		
		// Checks basic requirements
		Assert.notNull(admin, "El usuario no se ha logueado correctamente.");
		
		// Execution of test
		leagues = leagueService.findAll();
		
		league = null;
		for(League l: leagues){
//			if(l.getReferee() == referee){
				league = l; // Liga que dirige el referee1
				break;
//			}
		}
		
		Assert.notNull(league, "No hay ninguna liga para el referee1 para testear.");
		
		club = clubService.findAllByLeagueId(league.getId()).iterator().next(); // Un club que participa en esa liga
		
		classifications = club.getClassifications(); // Clasificaciones del club en todas las ligas que ha perticipados
		
		classification = null;
		for(Classification c: classifications){
			if(c.getRace().getLeague() == league){
				classification = c; // La clasificación del club en la liga seleccionada anteriormente
				break;
			}
		}
		
		Assert.notNull(classification, "No hay ninguna clasificación como con la que se pretende testear.");
		
		points = classification.getPoints();
		
		classification.setPoints(999999999); // Asignamos puntos errónos
		
//		classifications = club.getClassifications();
		
		clubService.calculateRankingByLeague(league.getId()); // Recalculamos los puntos
		
//		club = clubService.findOne(club.getId());
		
		newClassifications = club.getClassifications();
		
		classification = null;
		for(Classification c: newClassifications){
			if(c.getRace().getLeague() == league){
				classification = c;
				break;
			}
		}
		
		// Checks results
		Assert.isTrue(classification.getPoints() == points, "Las clasificaciones no se han actualizado correctamente.");
		Assert.isTrue(classification.getPoints() != 999999999, "La clasificación del club mantiene los puntos editados a mano.");
		
		unauthenticate();

	}

	/**
	 * @see 19.c
	 *  Un usuario que no haya iniciado sesión en el sistema debe poder:
	 *  Listar las distintas ligas, 
	 *  ver la clasificación, 
	 *  las carreras que la componen, 
	 *  los clubes que participan en ella y 
	 *  el árbitro que la dirige.
	 *  
	 *  Positive test: Se muestra la clasificación de una liga.
	 */
	@Test
	public void testListClassificationPerLeague1(){
		
		// Declare variable
		Collection<ArrayList<Integer>> result;
		Collection<League> allLeagues;
		Collection<Club> allClubs;
		League league;
		Club club = null;
		ArrayList<Integer> resultByClub;
		
		// Load object to test
		allLeagues = leagueService.findAll();
		allClubs = clubService.findAll();
		league = allLeagues.iterator().next();
		for(Club c:allClubs){
			if(c.getName().equals("Triana OC")){
				club = c;
			}
		}
		result = clubService.calculateRankingByLeague(league.getId());
		
		// Execution of test
		resultByClub = result.iterator().next();
			
		// Check result
		Assert.isTrue(resultByClub.get(0) == 0);
		Assert.isTrue(resultByClub.get(1) == club.getId());
		Assert.isTrue(resultByClub.get(2) == 663);
		leagueService.flush();
		clubService.flush();
		
	}
	
	/**
	 * @see 20.a
	 *  Un usuario autenticado en el sistema debe poder:
	 *  Listar las distintas ligas, 
	 *  ver la clasificación, 
	 *  las carreras que la componen, 
	 *  los clubes que participan en ella y 
	 *  el árbitro que la dirige.
	 *  
	 *  Positive test: Se muestra la clasificación de una liga.
	 */
	@Test
	public void testListClassificationPerLeague2(){
		
		// Declare variable
		Collection<ArrayList<Integer>> result;
		Collection<League> allLeagues;
		Collection<Club> allClubs;
		League league;
		Club club = null;
		ArrayList<Integer> resultByClub;
		
		// Load object to test
		authenticate("runner1");
		allLeagues = leagueService.findAll();
		allClubs = clubService.findAll();
		league = allLeagues.iterator().next();
		for(Club c:allClubs){
			if(c.getName().equals("Triana OC")){
				club = c;
			}
		}
		result = clubService.calculateRankingByLeague(league.getId());
		
		// Execution of test
		resultByClub = result.iterator().next();
			
		// Check result
		Assert.isTrue(resultByClub.get(0) == 0);
		Assert.isTrue(resultByClub.get(1) == club.getId());
		Assert.isTrue(resultByClub.get(2) == 663);
		unauthenticate();
		leagueService.flush();
		clubService.flush();
		
	}
}
