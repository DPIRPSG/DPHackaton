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

import domain.Bulletin;
import domain.Club;
import domain.League;
import domain.Manager;
import domain.Runner;

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
	
	/**
	 * @see 22.b
	 *  Un usuario que haya iniciado sesión como gerente debe poder:
	 *  Manejar el club, lo cual incluye editarlo y borrarlo.
	 *  
	 *  Positive test: El club se edita correctamente.
	 */
	@Test
	public void testEditClub1(){
		
		// Declare variable
		Collection<Club> result;
		Manager manager;
		Club club;
		
		// Load object to test
		authenticate("manager1");
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isTrue(result.contains(club));
		
		// Execution of test
		club.setName("Modificado");
		clubService.save(club);
		clubService.flush();

		// Check result
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isTrue(club.getName().equals("Modificado"));
		unauthenticate();
		
	}
	
	/**
	 * @see 22.b
	 *  Un usuario que haya iniciado sesión como gerente debe poder:
	 *  Manejar el club, lo cual incluye editarlo y borrarlo.
	 *  
	 *  Negative test: El club no se edita correctamente porque no estamos logueados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditClub2(){
		
		// Declare variable
		Collection<Club> result;
		Manager manager;
		Club club;
		
		// Load object to test
		//authenticate("manager1");
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isTrue(result.contains(club));
		
		// Execution of test
		club.setName("Modificado");
		clubService.save(club);
		clubService.flush();


		// Check result
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isTrue(club.getName().equals("Modificado"));
		//unauthenticate();
		
	}
	
	/**
	 * @see 22.b
	 *  Un usuario que haya iniciado sesión como gerente debe poder:
	 *  Manejar el club, lo cual incluye editarlo y borrarlo.
	 *  
	 *  Negative test: El club no se edita correctamente porque el manager no dirije ese club.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditClub3(){
		
		// Declare variable
		Collection<Club> result;
		Manager manager;
		Club club = null;
		
		// Load object to test
		authenticate("manager1");
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		for(Club c:result){
			if(!c.getName().equals("Los Imperdibles")){
				club = c;
			}
		}
		Assert.isTrue(result.contains(club));
		
		// Execution of test
		club.setName("Modificado");
		clubService.save(club);
		clubService.flush();

		// Check result
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isTrue(club.getName().equals("Modificado"));
		unauthenticate();
		
	}
	
	/**
	 * @see 22.b
	 *  Un usuario que haya iniciado sesión como gerente debe poder:
	 *  Manejar el club, lo cual incluye editarlo y borrarlo.
	 *  
	 *  Positive test: El club se cambia de manos correctamente.
	 */
	@Test
	public void testDeleteClub1(){
		
		// Declare variable
		Collection<Club> result;
		Collection<Manager> allManagerWithoutClub;
		Manager manager;
		Manager managerWithoutClub;
		Club club;
		
		// Load object to test
		authenticate("manager1");
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		allManagerWithoutClub = managerService.findAllWithoutClub();
		managerWithoutClub = allManagerWithoutClub.iterator().next();
		Assert.isTrue(result.contains(club));
		Assert.isTrue(club.getName().equals("Los Imperdibles"));
		Assert.notNull(managerWithoutClub);
		
		// Execution of test
		clubService.delete(club, managerWithoutClub.getId());
		clubService.flush();

		// Check result
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isNull(club);
		managerWithoutClub = managerService.findOne(managerWithoutClub.getId());
		club = managerWithoutClub.getClub();
		Assert.isTrue(club.getName().equals("Los Imperdibles"));
		unauthenticate();
		
	}
	
	/**
	 * @see 22.b
	 *  Un usuario que haya iniciado sesión como gerente debe poder:
	 *  Manejar el club, lo cual incluye editarlo y borrarlo.
	 *  
	 *  Negative test: El club se no cambia de manos correctamente ya que no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteClub2(){
		
		// Declare variable
		Collection<Club> result;
		Collection<Manager> allManagerWithoutClub;
		Manager manager;
		Manager managerWithoutClub;
		Club club;
		
		// Load object to test
		//authenticate("manager1");
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		allManagerWithoutClub = managerService.findAllWithoutClub();
		managerWithoutClub = allManagerWithoutClub.iterator().next();
		Assert.isTrue(result.contains(club));
		Assert.isTrue(club.getName().equals("Los Imperdibles"));
		Assert.notNull(managerWithoutClub);
		
		// Execution of test
		clubService.delete(club, managerWithoutClub.getId());
		clubService.flush();

		// Check result
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isNull(club);
		managerWithoutClub = managerService.findOne(managerWithoutClub.getId());
		club = managerWithoutClub.getClub();
		Assert.isTrue(club.getName().equals("Los Imperdibles"));
		//unauthenticate();
		
	}
	
	/**
	 * @see 22.b
	 *  Un usuario que haya iniciado sesión como gerente debe poder:
	 *  Manejar el club, lo cual incluye editarlo y borrarlo.
	 *  
	 *  Negative test: El club no se cambia de manos correctamente porque el nuevo manager no ya tiene un club.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteClub3(){
		
		// Declare variable
		Collection<Club> result;
		Manager manager;
		Manager managerWithClub;
		Club club;
		
		// Load object to test
		authenticate("manager2");
		managerWithClub = managerService.findByPrincipal();
		unauthenticate();
		authenticate("manager1");
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isTrue(result.contains(club));
		Assert.isTrue(club.getName().equals("Los Imperdibles"));
		Assert.notNull(managerWithClub);
		
		// Execution of test
		clubService.delete(club, managerWithClub.getId());
		clubService.flush();

		// Check result
		result = clubService.findAll();
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		Assert.isNull(club);
		managerWithClub = managerService.findOne(managerWithClub.getId());
		club = managerWithClub.getClub();
		Assert.isTrue(club.getName().equals("Los Imperdibles"));
		unauthenticate();
		
	}
	
}
