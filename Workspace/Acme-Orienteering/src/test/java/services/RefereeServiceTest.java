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
import domain.Referee;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class RefereeServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private RefereeService refereeService;
	
	// Other services needed -----------------------
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
	 *  Positive test: Se muestran el árbitro de la liga coleccionada.
	 */
	@Test
	public void testListRefereeByLeague1(){
		
		// Declare variable
		Referee result;
		Collection<League> allLeagues;
		League league;
		
		// Load object to test
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = league.getReferee();
		
		// Check result
		Assert.isTrue(result.getName().equals("Carlos"));
		leagueService.flush();
		
	}
	
}
