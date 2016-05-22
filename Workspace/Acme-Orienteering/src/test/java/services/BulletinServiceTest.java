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
import domain.Runner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BulletinServiceTest extends AbstractTest{
	
	// Service under test -------------------------
	@Autowired
	private BulletinService bulletinService;
	
	// Other services needed -----------------------
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ClubService clubService;
	
	
	// Tests ---------------------------------------
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Positive test: Se crea un boletín correctamente.
	 */
	@Test
	public void testCreateBulletin1(){
		
		// Declare variable
		Collection<Bulletin> result;
		Bulletin bulletin;
		Runner runner;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 1);
		
		// Execution of test
		bulletin = bulletinService.create();
		bulletin.setTitle("Prueba");
		bulletin.setDescription("Prueba");
		bulletinService.save(bulletin);	
		
		// Check result
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 2);
		unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
		
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se crea un boletín correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateBulletin2(){
		
		// Declare variable
		Collection<Bulletin> result;
		Bulletin bulletin;
		Runner runner;
		Club club;
		
		// Load object to test
		//authenticate("runner1");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 1);
		
		// Execution of test
		bulletin = bulletinService.create();
		bulletin.setTitle("Prueba");
		bulletin.setDescription("Prueba");
		bulletinService.save(bulletin);	
		
		// Check result
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 2);
		//unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
		
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se crea un boletín correctamente porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateBulletin3(){
		
		// Declare variable
		Collection<Bulletin> result;
		Bulletin bulletin;
		Runner runner;
		Club club;
		
		// Load object to test
		//authenticate("runner1");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 1);
		
		// Execution of test
		bulletin = bulletinService.create();
		bulletin.setTitle("Prueba");
		bulletin.setDescription("Prueba");
		bulletinService.save(bulletin);	
		
		// Check result
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 2);
		//unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
		
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se crea un boletín correctamente porque dejamos los huecos en blanco.
	 */
	@Test(expected = ConstraintViolationException.class)
	@Rollback(value = true)
	public void testCreateBulletin4(){
		
		// Declare variable
		Collection<Bulletin> result;
		Bulletin bulletin;
		Runner runner;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 1);
		
		// Execution of test
		bulletin = bulletinService.create();
		//bulletin.setTitle("Prueba");
		//bulletin.setDescription("Prueba");
		bulletinService.save(bulletin);	
		
		// Check result
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 2);
		unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
		
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se crea un boletín correctamente porque no es nuestro club.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testCreateBulletin5(){
		
		// Declare variable
		Collection<Bulletin> result;
		Collection<Club> allClubs;
		Bulletin bulletin;
		Runner runner;
		Club club;
		Club fakeClub = null;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		for(Club c:allClubs){
			if(c.getId() != club.getId()){
				fakeClub = c;
			}
		}
		result = bulletinService.findAllByClubId(fakeClub.getId());
		Assert.isTrue(result.size() == 1);
		
		// Execution of test
		bulletin = bulletinService.create();
		//bulletin.setTitle("Prueba");
		//bulletin.setDescription("Prueba");
		bulletinService.save(bulletin);	
		
		// Check result
		result = bulletinService.findAllByClubId(club.getId());
		Assert.isTrue(result.size() == 2);
		unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
		
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Positive test: Se muestran los boletines del club.
	 */
	@Test
	public void testListBulletinByClub1(){
		
		// Declare variable
		Collection<Bulletin> result;
		Runner runner;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		
		// Execution of test
		result = bulletinService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se muestran los boletines del club porque no estamos autenticados.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testListBulletinByClub2(){
		
		// Declare variable
		Collection<Bulletin> result;
		Runner runner;
		Club club;
		
		// Load object to test
		//authenticate("runner1");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		
		// Execution of test
		result = bulletinService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		//unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se muestran los boletines del club porque el corredor no tiene club.
	 */
	@Test(expected = NullPointerException.class)
	@Rollback(value = true)
	public void testListBulletinByClub3(){
		
		// Declare variable
		Collection<Bulletin> result;
		Runner runner;
		Club club;
		
		// Load object to test
		authenticate("runner2");
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		
		// Execution of test
		result = bulletinService.findAllByClubId(club.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
	}
	
	/**
	 * @see 21.c
	 *  Un usuario que haya iniciado sesión como corredor debe poder:
	 *  Crear y ver boletines en el tablón de su club.
	 *  
	 *  Negative test: No se muestran los boletines del club porque no es su club.
	 */
	@Test(expected = IllegalArgumentException.class)
	@Rollback(value = true)
	public void testListBulletinByClub4(){
		
		// Declare variable
		Collection<Bulletin> result;
		Collection<Club> allClubs;
		Runner runner;
		Club club;
		Club fakeClub = null;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		for(Club c:allClubs){
			if(c.getId() != club.getId()){
				fakeClub = c;
			}
		}
		
		// Execution of test
		result = bulletinService.findAllByClubId(fakeClub.getId());
		
		// Check result
		Assert.isTrue(result.size() == 1);
		unauthenticate();
		runnerService.flush();
		clubService.flush();
		bulletinService.flush();
	}
	
}
