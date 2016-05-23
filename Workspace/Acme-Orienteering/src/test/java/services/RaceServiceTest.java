package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Club;
import domain.League;
import domain.Race;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class RaceServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private RaceService raceService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Tests ---------------------------------------

	/**
	 * @see 19.b
	 *  Un usuario que no haya iniciado sesión en el sistema debe poder:
	 *  Listar los distintos clubes y
	 *  poder ver los corredores, y toda la información sobre estos (incluyendo su currículo), que están en dicho club, 
	 *  el gerente de este, y 
	 *  en que ligas y 
	 *  carreras han participado y 
	 *  están participando.
	 *  
	 *  Positive test: Se muestran las carreras del club seleccionado.
	 */
	@Test
	public void testListRaceByClub1(){
		
		// Declare variable
		Collection<Race> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = raceService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		raceService.flush();
		clubService.flush();
		
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
	 *  Positive test: Se muestran las carreras de la liga coleccionada.
	 */
	@Test
	public void testListRaceByLeague1(){
		
		// Declare variable
		Collection<Race> result;
		Collection<League> allLeagues;
		League league; 
		
		// Load object to test
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = league.getRacing();
		
		// Check result
		Assert.isTrue(result.size() == 3);
		leagueService.flush();
		
	}
	
	/**
	 * @see 20.a
	 *  Un usuario autenticado en el sistema debe poder:
	 *  Listar los distintos clubes y
	 *  poder ver los corredores, y toda la información sobre estos (incluyendo su currículo), que están en dicho club, 
	 *  el gerente de este, y 
	 *  en que ligas y 
	 *  carreras han participado y 
	 *  están participando.
	 *  
	 *  Positive test: Se muestran las carreras del club seleccionado.
	 */
	@Test
	public void testListRaceByClub2(){
		
		// Declare variable
		Collection<Race> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = raceService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		unauthenticate();
		raceService.flush();
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
	 *  Positive test: Se muestran las carreras de la liga coleccionada.
	 */
	@Test
	public void testListRaceByLeague2(){
		
		// Declare variable
		Collection<Race> result;
		Collection<League> allLeagues;
		League league; 
		
		// Load object to test
		authenticate("runner1");
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = league.getRacing();
		
		// Check result
		Assert.isTrue(result.size() == 3);
		unauthenticate();
		leagueService.flush();
		
	}
}
