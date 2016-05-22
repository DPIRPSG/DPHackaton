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

import controllers.administrator.ManagerAdministratorController;

import domain.Club;
import domain.FeePayment;
import domain.League;
import domain.Manager;
import domain.Punishment;
import domain.Referee;
import domain.form.FeePaymentForm;
import services.form.FeePaymentFormService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class FeePaymentServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private FeePaymentService feePaymentService;
	
	@Autowired
	private FeePaymentFormService feePaymentFormService;
	
	// Other services needed -----------------------
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private RefereeService refereeService;
	
	@Autowired
	private ManagerService managerService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager puede apuntar
	 * a su club a una liga en condiciones normales
	 */
	@Test
	public void testCreateFeePaymentOk() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager1");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}
		league = leagues.iterator().next();
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		feePaymentForm.setHolderName("Test");
		feePaymentForm.setBrandName("Test");
		feePaymentForm.setCvvCode(444);
		feePaymentForm.setExpirationMonth(12);
		feePaymentForm.setExpirationYear(2017);
		feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
}
