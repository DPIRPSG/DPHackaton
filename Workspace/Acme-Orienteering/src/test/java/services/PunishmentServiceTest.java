package services;


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
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * 
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
		
		System.out.println("numPreSaveClub: "+numPreSaveClub);
		System.out.println("numPostSaveClub: "+numPostSaveClub);
		System.out.println("numPreSaveLeague: "+numPreSaveLeague);
		System.out.println("numPostSaveLeague: "+numPostSaveLeague);
		
		Assert.isTrue((numPreSaveClub + 1) == numPostSaveClub);
		Assert.isTrue((numPreSaveLeague + 1) == numPostSaveLeague);
		Assert.isTrue(club.getPunishments().contains(punishment));
		Assert.isTrue(league.getPunishments().contains(punishment));
		
		authenticate(null);
	}
	
}
