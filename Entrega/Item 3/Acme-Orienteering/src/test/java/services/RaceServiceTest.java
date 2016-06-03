package services;

import java.util.Collection;
import java.util.Date;

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

import domain.Category;
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
	
	@Autowired
	private CategoryService categoryService;
	
	// Tests ---------------------------------------

	/**
	 * @see 1.B
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
		
	}
	
	/**
	 * @see 1.C
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
		Assert.isTrue(result.size() == 6);
		
	}
	
	/**
	 * @see 2.A
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

		
	}
	
	/**
	 * @see 2.A
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
		Assert.isTrue(result.size() == 6);
		unauthenticate();
		
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: Se muestran todos las carreras del sistema.
	 */
	public void testListRace1(){
		
		// Declare variable
		Collection<Race> result;
		
		// Load object to test
		authenticate("admin");
		
		// Execution of test
		result = raceService.findAll();
		
		// Check result
		Assert.isTrue(result.size() == 4);
		unauthenticate();
		
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera se crea correctamente.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testCreateRace1(){
		
		// Declare variable
		Collection<Race> result;
		Race race;
		Collection<League> allLeagues;
		League league = null;
		Collection<Category> allCategories;
		Category category = null;
		Date date;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		Assert.isTrue(result.size() == 8);
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		date = new Date();
		date.setYear(120);
		
		// Execution of test
		race = raceService.create();
		race.setName("Prueba");
		race.setDescription("Prueba");
		race.setMoment(date);
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 9);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se crea correctamente porque estamos autenticados como corredor.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateRace2(){
		
		// Declare variable
		Collection<Race> result;
		Race race;
		Collection<League> allLeagues;
		League league = null;
		Collection<Category> allCategories;
		Category category = null;
		Date date;
		
		// Load object to test
		authenticate("runner1");
		result = raceService.findAll();
		Assert.isTrue(result.size() == 4);
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		date = new Date();
		date.setYear(120);
		
		// Execution of test
		race = raceService.create();
		race.setName("Prueba");
		race.setDescription("Prueba");
		race.setMoment(date);
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 5);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se crea correctamente porque estamos autenticados como gerente.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateRace3(){
		
		// Declare variable
		Collection<Race> result;
		Race race;
		Collection<League> allLeagues;
		League league = null;
		Collection<Category> allCategories;
		Category category = null;
		Date date;
		
		// Load object to test
		authenticate("manager1");
		result = raceService.findAll();
		Assert.isTrue(result.size() == 4);
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		date = new Date();
		date.setYear(120);
		
		// Execution of test
		race = raceService.create();
		race.setName("Prueba");
		race.setDescription("Prueba");
		race.setMoment(date);
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 5);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se crea correctamente porque estamos autenticados como árbitro.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateRace4(){
		
		// Declare variable
		Collection<Race> result;
		Race race;
		Collection<League> allLeagues;
		League league = null;
		Collection<Category> allCategories;
		Category category = null;
		Date date;
		
		// Load object to test
		authenticate("referee1");
		result = raceService.findAll();
		Assert.isTrue(result.size() == 4);
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		date = new Date();
		date.setYear(120);
		
		// Execution of test
		race = raceService.create();
		race.setName("Prueba");
		race.setDescription("Prueba");
		race.setMoment(date);
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 5);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se crea correctamente porque dejamos espacios en blanco.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateRace5(){
		
		// Declare variable
		Collection<Race> result;
		Race race;
		Collection<League> allLeagues;
		League league = null;
		Collection<Category> allCategories;
		Category category = null;
		Date date;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		Assert.isTrue(result.size() == 8);
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		date = new Date();
		date.setYear(120);
		
		// Execution of test
		race = raceService.create();
		//race.setName("Prueba");
		//race.setDescription("Prueba");
		race.setMoment(date);
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 9);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se crea correctamente porque ponemos una fecha en el pasado.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateRace6(){
		
		// Declare variable
		Collection<Race> result;
		Race race;
		Collection<League> allLeagues;
		League league = null;
		Collection<Category> allCategories;
		Category category = null;
		Date date;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		Assert.isTrue(result.size() == 4);
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getName().equals("Junior")){
				category = c;
			}
		}
		
		date = new Date();
		date.setYear(100);
		
		// Execution of test
		race = raceService.create();
		race.setName("Prueba");
		race.setDescription("Prueba");
		race.setMoment(date);
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 5);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera se edita correctamente.
	 */
	@Test
	public void testEditRace1(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		Race race2 = null;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Round 1")){
				race = r;
			}
		}
		
		// Execution of test
		race.setName("Prueba");
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Prueba")){
				race2 = r;
			}
		}
		Assert.notNull(race2);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La carrera no se edita correctamente porque ponemos una fecha de comienzo del pasado.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditRace2(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		Race race2 = null;
		Date date;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Round 1")){
				race = r;
			}
		}
		date = new Date();
		date.setYear(100);
		
		// Execution of test
		race.setMoment(date);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Prueba")){
				race2 = r;
			}
		}
		Assert.notNull(race2);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La carrera no se edita correctamente porque no tiene categoría ni liga.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditRace3(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		Race race2 = null;
		Collection<Category> allCategories;
		Category category = null;
		Collection<League> allLeagues;
		League league = null;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Round 1")){
				race = r;
			}
		}
		allCategories = categoryService.findAll();
		for(Category c:allCategories){
			if(c.getId() != race.getCategory().getId()){
				category = c;
			}
		}
		
		allLeagues = leagueService.findAll();
		for(League l:allLeagues){
			if(l.getId() != race.getLeague().getId()){
				league = l;
			}
		}
		
		// Execution of test
		race.setCategory(category);
		race.setLeague(league);
		raceService.save(race);
		raceService.flush();
		
		// Check result
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Prueba")){
				race2 = r;
			}
		}
		Assert.notNull(race2);
		unauthenticate();
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera se borra correctamente.
	 */
	@Test
	public void testDeleteRace1(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		
		// Load object to test
		authenticate("admin");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getClassifications().isEmpty() && r.getParticipates().isEmpty()){
				race = r;
			}
		}
		Assert.isTrue(result.size() == 8);
		
		// Execution of test
		raceService.delete(race);
		raceService.flush();

		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 7);
		unauthenticate();	
		
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se borra correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteRace2(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		
		// Load object to test
		//authenticate("admin");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Primera carrera de la liga junior")){
				race = r;
			}
		}
		Assert.isTrue(result.size() == 4);
		
		// Execution of test
		raceService.delete(race);
		raceService.flush();

		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 3);
		//unauthenticate();	
		
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se borra correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteRace3(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		
		// Load object to test
		authenticate("runner1");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Primera carrera de la liga junior")){
				race = r;
			}
		}
		Assert.isTrue(result.size() == 4);
		
		// Execution of test
		raceService.delete(race);
		raceService.flush();

		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 3);
		unauthenticate();	
		
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se borra correctamente porque estamos autenticados como gerente.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteRace4(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		
		// Load object to test
		authenticate("manager1");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Primera carrera de la liga junior")){
				race = r;
			}
		}
		Assert.isTrue(result.size() == 4);
		
		// Execution of test
		raceService.delete(race);
		raceService.flush();

		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 3);
		unauthenticate();	
		
	}
	
	/**
	 * @see 6.D
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las carreras. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La carrera no se borra correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteRace5(){
		
		// Declare variable
		Collection<Race> result;
		Race race = null;
		
		// Load object to test
		authenticate("referee1");
		result = raceService.findAll();
		for(Race r:result){
			if(r.getName().equals("Primera carrera de la liga junior")){
				race = r;
			}
		}
		Assert.isTrue(result.size() == 4);
		
		// Execution of test
		raceService.delete(race);
		raceService.flush();

		
		// Check result
		result = raceService.findAll();
		Assert.isTrue(result.size() == 3);
		unauthenticate();	
		
	}
	
}
