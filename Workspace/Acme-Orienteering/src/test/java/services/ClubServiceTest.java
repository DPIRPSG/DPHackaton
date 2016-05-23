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

import domain.Club;
import domain.League;
import domain.Manager;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class ClubServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private ClubService clubService;
	
	// Other services needed -----------------------
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ManagerService managerService;
	
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
	 *  Positive test: Se muestran los clubes.
	 */
	@Test
	public void testListClub1(){
		
		// Declare variable
		Collection<Club> result;
		
		// Load object to test
		
		// Execution of test
		result = clubService.findAll();
		
		// Check result
		Assert.isTrue(result.size() == 2);
		clubService.flush();
		
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
	 *  Positive test: Se muestran los clubes.
	 */
	@Test
	public void testListClub2(){
		
		// Declare variable
		Collection<Club> result;
		
		// Load object to test
		authenticate("runner1");
		
		// Execution of test
		result = clubService.findAll();
		
		// Check result
		Assert.isTrue(result.size() == 2);
		unauthenticate();
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
	 *  Positive test: Se muestran los clubes de la liga coleccionada.
	 */
	@Test
	public void testListClubByLeague1(){
		
		// Declare variable
		Collection<Club> result;
		Collection<League> allLeagues;
		League league;
		
		// Load object to test
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = clubService.findAllByLeagueId(league.getId());
		
		// Check result
		Assert.isTrue(result.size() == 2);
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
	 *  Positive test: Se muestran los clubes de la liga coleccionada.
	 */
	@Test
	public void testListClubByLeague2(){
		
		// Declare variable
		Collection<Club> result;
		Collection<League> allLeagues;
		League league;
		
		// Load object to test
		authenticate("runner1");
		allLeagues = leagueService.findAll();
		league = allLeagues.iterator().next();
		
		// Execution of test
		result = clubService.findAllByLeagueId(league.getId());
		
		// Check result
		Assert.isTrue(result.size() == 2);
		unauthenticate();
		leagueService.flush();
		clubService.flush();
	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club se ha creado correctamente.
	 */
	@Test
	public void testCreateClub1(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("manager3");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		System.out.println(manager);
		System.out.println(manager.getClub());
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();
		clubService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club no se ha creado correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateClub2(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		//authenticate("manager3");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		//unauthenticate();
		managerService.flush();
		clubService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club no se ha creado correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateClub3(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();
		clubService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club no se ha creado correctamente porque estamos autenticados como administrador.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateClub4(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("admin");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();
		clubService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club no se ha creado correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateClub5(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("admin");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();
		clubService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club no se ha creado correctamente porque hemos dejado atributos sin poner.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateClub6(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("manager3");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		//club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		clubService.flush();
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Positive test: El club no se ha creado correctamente porque hemos dejado atributos sin poner.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateClub7(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("manager3");
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		//club.setDescription("Prueba");
		clubService.save(club);
		clubService.flush();
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();

	}
	
	/**
	 * @see 22.a
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Crear un club, siempre que no sea el gerente de otro.
	 * 
	 * Negative test: El club no se ha creado correctamente porque el manager ya posee otro club.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateClub8(){
		
		// Declare variable
		Club result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("manager1");
		manager = managerService.findByPrincipal();
		//result = manager.getClub();
		//Assert.isNull(result);
		
		// Execution of test
		club = clubService.create();
		club.setName("Prueba");
		club.setDescription("Prueba");
		clubService.save(club);
		
		// Check result
		manager = managerService.findByPrincipal();
		result = manager.getClub();
		Assert.notNull(result);
		Assert.isTrue(manager.getClub().getName().equals("Prueba"));
		Assert.isTrue(manager.getClub().getDescription().equals("Prueba"));
		unauthenticate();
		managerService.flush();
		clubService.flush();

	}
	
}
