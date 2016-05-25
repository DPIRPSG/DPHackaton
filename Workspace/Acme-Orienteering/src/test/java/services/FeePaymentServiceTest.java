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

import domain.FeePayment;
import domain.League;
import domain.Manager;
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
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}
		
		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
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
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que si se intenta apuntar
	 * a un club a una liga sin estar logueado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError1() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		authenticate(null);
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}

		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
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
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba si se intenta apuntar
	 * a un club a una liga sin ser el manager, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError2() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		authenticate(null);
		
		authenticate("manager1");
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}

		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
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
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que si un manager intenta apuntar
	 * a su club a una liga en la que ya esta apuntado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError3() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager1");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		league = manager.getClub().getFeePayments().iterator().next().getLeague();
		
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
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager no puede apuntar
	 * a su club a una liga sin introducir el Holder Name
	 * de la tarjeta de credito
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError4() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}

		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		//feePaymentForm.setHolderName("Test");
		feePaymentForm.setBrandName("Test");
		feePaymentForm.setCvvCode(444);
		feePaymentForm.setExpirationMonth(12);
		feePaymentForm.setExpirationYear(2017);
		feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		feePaymentService.flush();
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager no puede apuntar
	 * a su club a una liga sin introducir el Brand Name
	 * de la tarjeta de credito
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError5() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}

		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		feePaymentForm.setHolderName("Test");
		//feePaymentForm.setBrandName("Test");
		feePaymentForm.setCvvCode(444);
		feePaymentForm.setExpirationMonth(12);
		feePaymentForm.setExpirationYear(2017);
		feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		feePaymentService.flush();
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager no puede apuntar
	 * a su club a una liga sin introducir el CVV Code
	 * de la tarjeta de credito
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError6() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}

		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		feePaymentForm.setHolderName("Test");
		feePaymentForm.setBrandName("Test");
		//feePaymentForm.setCvvCode(444);
		feePaymentForm.setExpirationMonth(12);
		feePaymentForm.setExpirationYear(2017);
		feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		feePaymentService.flush();
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager no puede apuntar
	 * a su club a una liga sin introducir el mes de expiracion
	 * de la tarjeta de credito
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError7() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}
		
		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		feePaymentForm.setHolderName("Test");
		feePaymentForm.setBrandName("Test");
		feePaymentForm.setCvvCode(444);
		//feePaymentForm.setExpirationMonth(12);
		feePaymentForm.setExpirationYear(2017);
		feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		feePaymentService.flush();
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager no puede apuntar
	 * a su club a una liga sin introducir el año de expiracion
	 * de la tarjeta de credito
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError8() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}
		
		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		feePaymentForm.setHolderName("Test");
		feePaymentForm.setBrandName("Test");
		feePaymentForm.setCvvCode(444);
		feePaymentForm.setExpirationMonth(12);
		//feePaymentForm.setExpirationYear(2017);
		feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		feePaymentService.flush();
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un manager no puede apuntar
	 * a su club a una liga sin introducir el numero
	 * de la tarjeta de credito
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError9() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}
		
		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().after(new Date())) {
				league = l;
				break;
			}
		}
		
		numPreLeague = league.getFeePayments().size();
			
		feePaymentForm = feePaymentFormService.create(league.getId());
		feePaymentForm.setHolderName("Test");
		feePaymentForm.setBrandName("Test");
		feePaymentForm.setCvvCode(444);
		feePaymentForm.setExpirationMonth(12);
		feePaymentForm.setExpirationYear(2017);
		//feePaymentForm.setNumber("4719068196160163");
		feePayment = feePaymentFormService.reconstruct(feePaymentForm);
		feePaymentService.flush();
		
		numPostClub = manager.getClub().getFeePayments().size();
		
		league = leagueService.findOne(league.getId());
		numPostLeague = league.getFeePayments().size();
		
		Assert.isTrue(manager.getClub().getFeePayments().contains(feePayment));
		Assert.isTrue(league.getFeePayments().contains(feePayment));
		Assert.isTrue((numPreClub +1) == numPostClub);
		Assert.isTrue((numPreLeague +1) == numPostLeague);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que si un manager intenta apuntar
	 * a su club a una liga ya empezada, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateFeePaymentError10() {
		FeePaymentForm feePaymentForm;
		FeePayment feePayment;
		Collection<League> leagues;
		League league;
		Manager manager;
		int numPreClub, numPostClub;
		int numPreLeague, numPostLeague;
		
		authenticate("manager2");
		manager = managerService.findByPrincipal();
		
		numPreClub = manager.getClub().getFeePayments().size();
		
		leagues = leagueService.findAll();
		for(FeePayment fee : manager.getClub().getFeePayments()) {
			leagues.remove(fee.getLeague());
		}
		
		league = null;
		for(League l : leagues) {
			if(l.getStartedMoment().before(new Date())) {
				league = l;
				break;
			}
		}
		
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
