package services;

import java.util.Collection;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
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
public class LeagueServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private LeagueService leagueService;
	
	// Other services needed -----------------------
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private RefereeService refereeService;
	
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
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga se crea correctamente.
	 */
	@Test
	public void testCreateLeague1(){
		
		// Declare variable
		Collection<League> result;
		League league;
		Collection<Referee> allReferees;
		Referee referee = null;
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		allReferees = refereeService.findAll();
		for(Referee r:allReferees){
			if(r.getName().equals("Carlos")){
				referee = r;
				break;
			}
		}
		Assert.isTrue(result.size() == 3);
		
		// Execution of test
		league = leagueService.create();
		league.setName("Prueba");
		league.setDescription("Prueba");
		league.setStartedMoment(new Date());
		league.setAmount(50.0);
		league.setReferee(referee);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 4);
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La liga no se crea correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateLeague2(){
		
		// Declare variable
		Collection<League> result;
		League league;
		Collection<Referee> allReferees;
		Referee referee = null;
		
		// Load object to test
		//authenticate("admin");
		result = leagueService.findAll();
		allReferees = refereeService.findAll();
		for(Referee r:allReferees){
			if(r.getName().equals("Carlos")){
				referee = r;
				break;
			}
		}
		Assert.isTrue(result.size() == 3);
		
		// Execution of test
		league = leagueService.create();
		league.setName("Prueba");
		league.setDescription("Prueba");
		league.setStartedMoment(new Date());
		league.setAmount(50.0);
		league.setReferee(referee);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 4);
		//unauthenticate();
		
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La liga no se crea correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateLeague3(){
		
		// Declare variable
		Collection<League> result;
		League league;
		Collection<Referee> allReferees;
		Referee referee = null;
		
		// Load object to test
		authenticate("runner1");
		result = leagueService.findAll();
		allReferees = refereeService.findAll();
		for(Referee r:allReferees){
			if(r.getName().equals("Carlos")){
				referee = r;
				break;
			}
		}
		Assert.isTrue(result.size() == 3);
		
		// Execution of test
		league = leagueService.create();
		league.setName("Prueba");
		league.setDescription("Prueba");
		league.setStartedMoment(new Date());
		league.setAmount(50.0);
		league.setReferee(referee);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 4);
		unauthenticate();
		
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La liga no se crea correctamente porque estamos autenticados como gerente.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateLeague4(){
		
		// Declare variable
		Collection<League> result;
		League league;
		Collection<Referee> allReferees;
		Referee referee = null;
		
		// Load object to test
		authenticate("manager1");
		result = leagueService.findAll();
		allReferees = refereeService.findAll();
		for(Referee r:allReferees){
			if(r.getName().equals("Carlos")){
				referee = r;
				break;
			}
		}
		Assert.isTrue(result.size() == 3);
		
		// Execution of test
		league = leagueService.create();
		league.setName("Prueba");
		league.setDescription("Prueba");
		league.setStartedMoment(new Date());
		league.setAmount(50.0);
		league.setReferee(referee);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 4);
		unauthenticate();
		
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La liga no se crea correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateLeague5(){
		
		// Declare variable
		Collection<League> result;
		League league;
		Collection<Referee> allReferees;
		Referee referee = null;
		
		// Load object to test
		authenticate("referee1");
		result = leagueService.findAll();
		allReferees = refereeService.findAll();
		for(Referee r:allReferees){
			if(r.getName().equals("Carlos")){
				referee = r;
				break;
			}
		}
		Assert.isTrue(result.size() == 3);
		
		// Execution of test
		league = leagueService.create();
		league.setName("Prueba");
		league.setDescription("Prueba");
		league.setStartedMoment(new Date());
		league.setAmount(50.0);
		league.setReferee(referee);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 4);
		unauthenticate();
		
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Negative Test: La liga no se crea correctamente porque no dejamos valores vacíos.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateLeague6(){
		
		// Declare variable
		Collection<League> result;
		League league;
		Collection<Referee> allReferees;
		Referee referee = null;
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		allReferees = refereeService.findAll();
		for(Referee r:allReferees){
			if(r.getName().equals("Carlos")){
				referee = r;
				break;
			}
		}
		Assert.isTrue(result.size() == 3);
		
		// Execution of test
		league = leagueService.create();
		//league.setName("Prueba");
		//league.setDescription("Prueba");
		league.setStartedMoment(new Date());
		league.setAmount(50.0);
		league.setReferee(referee);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 4);
		unauthenticate();
		
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga se edita correctamente.
	 */
	@Test
	public void testEditLeague1(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		for(League l:result){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		
		// Execution of test
		league.setName("Prueba");
		league.setDescription("Prueba");
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		for(League l:result){
			if(l.getName().equals("Prueba")){
				league = l;
			}
		}
		Assert.isTrue(league.getName().equals("Prueba"));
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se edita correctamente porque tiene una fecha de comienzo anterior a la actual.
	 */
	@SuppressWarnings("deprecation")
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testEditLeague2(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		Date oldDate;
		
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		for(League l:result){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		oldDate = league.getStartedMoment();
		oldDate.setYear(100);
		
		// Execution of test
		league.setStartedMoment(oldDate);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		for(League l:result){
			if(l.getName().equals("Prueba")){
				league = l;
			}
		}
		Assert.isTrue(league.getName().equals("Prueba"));
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se edita correctamente porque no tiene árbitro.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testEditLeague3(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		for(League l:result){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		
		// Execution of test
		league.setReferee(null);
		leagueService.save(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		for(League l:result){
			if(l.getName().equals("Prueba")){
				league = l;
			}
		}
		Assert.isTrue(league.getName().equals("Prueba"));
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga se borra correctamente.
	 */
	@Test
	public void testDeleteLeague1(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 3);
		for(League l:result){
			if(l.getName().equals("First Junior Orienteering American League (FJOAL)")){
				league = l;
			}
		}
		
		// Execution of test
		leagueService.delete(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 2);
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se borra correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteLeague2(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		//authenticate("admin");
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 3);
		for(League l:result){
			if(l.getName().equals("First Junior Orienteering American League (FJOAL)")){
				league = l;
			}
		}
		
		// Execution of test
		leagueService.delete(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 2);
		//unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se borra correctamente porque estamos autenticados como corredor.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteLeague3(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("runner1");
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 3);
		for(League l:result){
			if(l.getName().equals("First Junior Orienteering American League (FJOAL)")){
				league = l;
			}
		}
		
		// Execution of test
		leagueService.delete(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 2);
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se borra correctamente porque estamos autenticados como gerente.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteLeague4(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("manager1");
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 3);
		for(League l:result){
			if(l.getName().equals("First Junior Orienteering American League (FJOAL)")){
				league = l;
			}
		}
		
		// Execution of test
		leagueService.delete(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 2);
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se borra correctamente porque estamos autenticados como árbitro.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testDeleteLeague5(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("referee1");
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 3);
		for(League l:result){
			if(l.getName().equals("First Junior Orienteering American League (FJOAL)")){
				league = l;
			}
		}
		
		// Execution of test
		leagueService.delete(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 2);
		unauthenticate();
	}
	
	/**
	 * @see 24.c
	 *  Un usuario que haya iniciado sesión como administrador debe poder:
	 *  Manejar las ligas. Esto incluye crear, editar, borrar y listar.
	 *  
	 *  Positive Test: La liga no se borra correctamente porque está en curso.
	 */
	@Test(expected = DataIntegrityViolationException.class)
	@Rollback(value = true)
	public void testDeleteLeague6(){
		
		// Declare variable
		Collection<League> result;
		League league = null;
		
		
		// Load object to test
		authenticate("admin");
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 3);
		for(League l:result){
			if(l.getName().equals("First Orienteering League (FOL)")){
				league = l;
			}
		}
		
		// Execution of test
		leagueService.delete(league);
		leagueService.flush();
		
		// Check result
		result = leagueService.findAll();
		Assert.isTrue(result.size() == 2);
		unauthenticate();
	}
}
