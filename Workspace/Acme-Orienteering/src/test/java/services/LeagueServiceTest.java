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

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class LeagueServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private LeagueService leagueService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ClubService clubService;
	
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
	 *  Positive test: Se muestran las ligas del club seleccionado.
	 */
	@Test
	public void testListLeagueByClub1(){
		
		// Declare variable
		Collection<League> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = leagueService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		
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
	 *  Positive test: Se muestran las ligas.
	 */
	@Test
	public void testListLeague1(){
		
		// Declare variable
		Collection<League> result;
		
		// Load object to test
		
		// Execution of test
		result = leagueService.findAll();
		
		// Check result
		Assert.isTrue(result.size() == 3);
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
	 *  Positive test: Se muestran las ligas del club seleccionado.
	 */
	@Test
	public void testListLeagueByClub2(){
		
		// Declare variable
		Collection<League> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = leagueService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		unauthenticate();
		
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
	 *  Positive test: Se muestran las ligas.
	 */
	@Test
	public void testListLeague2(){
		
		// Declare variable
		Collection<League> result;
		
		// Load object to test
		authenticate("runner1");
		
		// Execution of test
		result = leagueService.findAll();
		
		// Check result
		Assert.isTrue(result.size() == 3);
		unauthenticate();
	}
}
