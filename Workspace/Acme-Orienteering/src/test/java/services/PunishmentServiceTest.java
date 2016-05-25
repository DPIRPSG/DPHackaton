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
import domain.Punishment;
import domain.Referee;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class PunishmentServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private PunishmentService punishmentService;
	
	// Other services needed -----------------------
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private RefereeService refereeService;
	
	// Tests ---------------------------------------
	
	/**
	 * @see 19.d
	 *  Un usuario que no haya iniciado sesión en el sistema debe poder:
	 *  Listar las distintas sanciones impuestas a los clubes en cada liga.
	 *  
	 *  Positive Test: Se muestran las sanciones impuestas a un club.
	 */
	@Test
	public void testListPunishmentByClub1(){
		
		// Declare variable
		Collection<Punishment> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = club.getPunishments();

		// Check result
		Assert.isTrue(result.size() == 6);

	}
	
	/**
	 * @see 20.a
	 *  Un usuario autenticado en el sistema debe poder:
	 *  Listar las distintas sanciones impuestas a los clubes en cada liga.
	 *  
	 *  Positive Test: Se muestran las sanciones impuestas a un club.
	 */
	@Test
	public void testListPunishmentByClub2(){
		
		// Declare variable
		Collection<Punishment> result;
		Collection<Club> allClubs;
		Club club;
		
		// Load object to test
		authenticate("runner1");
		allClubs = clubService.findAll();
		club = allClubs.iterator().next();
		
		// Execution of test
		result = club.getPunishments();

		// Check result
		Assert.isTrue(result.size() == 6);
		unauthenticate();

	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede crear
	 * una sancion en condiciones normales
	 */
	@Test
	public void testCreatePunishmentOk() {
		Punishment punishment;
		League league;
		Club club;
		Referee referee;
		int numPreSaveClub, numPostSaveClub;
		int numPreSaveLeague, numPostSaveLeague;
				
		league = leagueService.findAll().iterator().next();
		club = league.getFeePayments().iterator().next().getClub();
		referee = league.getReferee();
		
		numPreSaveClub = club.getPunishments().size();
		numPreSaveLeague = league.getPunishments().size();
		
		authenticate(referee.getUserAccount().getUsername());
		punishment = punishmentService.create(club.getId());
		punishment.setReason("Prueba");
		punishment.setPoints(10);
		punishment.setLeague(league);
		punishment = punishmentService.save(punishment);
		
		league = leagueService.findOne(league.getId());
		club = clubService.findOne(club.getId());
		
		numPostSaveClub = club.getPunishments().size();
		numPostSaveLeague = league.getPunishments().size();
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que si intentar crear
	 * una sancion sin ser arbitro, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreatePunishmentError1() {
		Punishment punishment;
		League league;
		Club club;
		//Referee referee;
		int numPreSaveClub, numPostSaveClub;
		int numPreSaveLeague, numPostSaveLeague;
				
		league = leagueService.findAll().iterator().next();
		club = league.getFeePayments().iterator().next().getClub();
		//referee = league.getReferee();
		
		numPreSaveClub = club.getPunishments().size();
		numPreSaveLeague = league.getPunishments().size();
		
		authenticate("manager2");
		punishment = punishmentService.create(club.getId());
		punishment.setReason("Prueba");
		punishment.setPoints(10);
		punishment.setLeague(league);
		punishment = punishmentService.save(punishment);
		
		league = leagueService.findOne(league.getId());
		club = clubService.findOne(club.getId());
		
		numPostSaveClub = club.getPunishments().size();
		numPostSaveLeague = league.getPunishments().size();
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que si se intenta crear
	 * una sancion sin ser el arbitro de dicha liga, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreatePunishmentError2() {
		Punishment punishment;
		League league;
		Club club;
		Collection<Referee> referees;
		Referee referee;
		int numPreSaveClub, numPostSaveClub;
		int numPreSaveLeague, numPostSaveLeague;
				
		league = leagueService.findAll().iterator().next();
		club = league.getFeePayments().iterator().next().getClub();
		referee = league.getReferee();
		
		referees = refereeService.findAll();
		for(Referee r : referees) {
			if(r.getId() != referee.getId()) {
				referee = r;
				break;
			}
		}
		
		numPreSaveClub = club.getPunishments().size();
		numPreSaveLeague = league.getPunishments().size();
		
		authenticate(referee.getUserAccount().getUsername());
		punishment = punishmentService.create(club.getId());
		punishment.setReason("Prueba");
		punishment.setPoints(10);
		punishment.setLeague(league);		
		punishment = punishmentService.save(punishment);
		
		league = leagueService.findOne(league.getId());
		club = clubService.findOne(club.getId());
		
		numPostSaveClub = club.getPunishments().size();
		numPostSaveLeague = league.getPunishments().size();
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear
	 * una sancion sin indicar la liga
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreatePunishmentError3() {
		Punishment punishment;
		League league;
		Club club;
		Referee referee;
		int numPreSaveClub, numPostSaveClub;
		int numPreSaveLeague, numPostSaveLeague;
				
		league = leagueService.findAll().iterator().next();
		club = league.getFeePayments().iterator().next().getClub();
		referee = league.getReferee();
		
		numPreSaveClub = club.getPunishments().size();
		numPreSaveLeague = league.getPunishments().size();
		
		authenticate(referee.getUserAccount().getUsername());
		punishment = punishmentService.create(club.getId());
		punishment.setReason("Prueba");
		punishment.setPoints(10);
		//punishment.setLeague(league);
		punishment = punishmentService.save(punishment);
		
		league = leagueService.findOne(league.getId());
		club = clubService.findOne(club.getId());
		
		numPostSaveClub = club.getPunishments().size();
		numPostSaveLeague = league.getPunishments().size();
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear
	 * una sancion sin indicar el motivo
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreatePunishmentError4() {
		Punishment punishment;
		League league;
		Club club;
		Referee referee;
		int numPreSaveClub, numPostSaveClub;
		int numPreSaveLeague, numPostSaveLeague;
				
		league = leagueService.findAll().iterator().next();
		club = league.getFeePayments().iterator().next().getClub();
		referee = league.getReferee();
		
		numPreSaveClub = club.getPunishments().size();
		numPreSaveLeague = league.getPunishments().size();
		
		authenticate(referee.getUserAccount().getUsername());
		punishment = punishmentService.create(club.getId());
		//punishment.setReason("Prueba");
		punishment.setPoints(10);
		punishment.setLeague(league);
		punishment = punishmentService.save(punishment);
		punishmentService.flush();
		
		league = leagueService.findOne(league.getId());
		club = clubService.findOne(club.getId());
		
		numPostSaveClub = club.getPunishments().size();
		numPostSaveLeague = league.getPunishments().size();
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear
	 * una sancion sin indicar los puntos
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreatePunishmentError5() {
		Punishment punishment;
		League league;
		Club club;
		Referee referee;
		int numPreSaveClub, numPostSaveClub;
		int numPreSaveLeague, numPostSaveLeague;
				
		league = leagueService.findAll().iterator().next();
		club = league.getFeePayments().iterator().next().getClub();
		referee = league.getReferee();
		
		numPreSaveClub = club.getPunishments().size();
		numPreSaveLeague = league.getPunishments().size();
		
		authenticate(referee.getUserAccount().getUsername());
		punishment = punishmentService.create(club.getId());
		punishment.setReason("Prueba");
		//punishment.setPoints(10);
		punishment.setLeague(league);
		punishment = punishmentService.save(punishment);
		punishmentService.flush();
		
		league = leagueService.findOne(league.getId());
		club = clubService.findOne(club.getId());
		
		numPostSaveClub = club.getPunishments().size();
		numPostSaveLeague = league.getPunishments().size();
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
}
