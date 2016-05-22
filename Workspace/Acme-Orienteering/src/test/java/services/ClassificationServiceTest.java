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

import domain.Club;
import domain.League;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ClassificationServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private ClassificationService classificationService;
	
	// Other services needed -----------------------
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Tests ---------------------------------------

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
}
