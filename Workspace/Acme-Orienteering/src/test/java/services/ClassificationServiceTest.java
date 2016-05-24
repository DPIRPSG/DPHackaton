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
	
	@Test 
	public void testUpdateClassification() {
		// Declare variables
		Actor referee;
		Collection<League> leagues;
		League league;
		Club club;
		Collection<Classification> classifications;
		Collection<Classification> newClassifications;
		Classification classification;
		
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
				league = l;
				break;
			}
		}
		
		Assert.notNull(league, "No hay ninguna liga para el referee1 para testear.");
		
		club = clubService.findAllByLeagueId(league.getId()).iterator().next();
		
		classifications = club.getClassifications();
		
		classification = null;
		for(Classification c: classifications){
			if(c.getRace().getLeague() == league){
				classification = c;
				break;
			}
		}
		
		Assert.notNull(classification, "No hay ninguna clasificación como con la que se pretende testear.");
		
		classification.setPoints(999999999);
		
		classifications = club.getClassifications();
		
		clubService.calculateRankingByLeague(league.getId());
		
//		club = clubService.findOne(club.getId());
		
		newClassifications = club.getClassifications();
		
		// Checks results
		Assert.isTrue(!classifications.equals(newClassifications), "Las clasificaciones no se han actualizado");
		
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
			if(c.getName().equals("Los Imperdibles")){
				club = c;
			}
		}
		result = clubService.calculateRankingByLeague(league.getId());
		
		// Execution of test
		resultByClub = result.iterator().next();
			
		// Check result
		Assert.isTrue(resultByClub.get(0) == 0);
		Assert.isTrue(resultByClub.get(1) == club.getId());
		Assert.isTrue(resultByClub.get(2) == 25);
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
			if(c.getName().equals("Los Imperdibles")){
				club = c;
			}
		}
		result = clubService.calculateRankingByLeague(league.getId());
		
		// Execution of test
		resultByClub = result.iterator().next();
			
		// Check result
		Assert.isTrue(resultByClub.get(0) == 0);
		Assert.isTrue(resultByClub.get(1) == club.getId());
		Assert.isTrue(resultByClub.get(2) == 25);
		unauthenticate();
		leagueService.flush();
		clubService.flush();
		
	}
}
