package services.form;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.ActorService;
import services.FeePaymentService;
import services.LeagueService;
import services.ManagerService;
import domain.Club;
import domain.CreditCard;
import domain.FeePayment;
import domain.League;
import domain.Manager;
import domain.form.FeePaymentForm;

@Service
@Transactional
public class FeePaymentFormService {

	// Managed repository -----------------------------------------------------

	
	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FeePaymentService feePaymentService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Constructors -----------------------------------------------------------
	
	public FeePaymentFormService(){
		super();
	}

	// Methods -----------------------------------------------------------

	public FeePaymentForm create(int leagueId) {
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can pay for a league.");
		Assert.notNull(leagueId, "You must choose a league to join your club to it.");
		
		FeePaymentForm result;
		Manager manager;
		League league;
		Club club;
		
		result = new FeePaymentForm();
		
		manager = managerService.findByPrincipal();
		
		league = leagueService.findOne(leagueId);
		
		club = manager.getClub();
		
		Assert.isTrue(!leagueService.findAllByClubId(club.getId()).contains(league), "Your club is already registered for this league.");
		
		result.setLeague(league);
		result.setClub(club);
		result.setPaymentMoment(new Date()); // Ponemos ahora la fecha porque no puede ser null, pero se debe poner la "real" en el save.
		
		return result;
	}



	public FeePayment reconstruct(FeePaymentForm feePaymentForm) {
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can pay for a league.");
		Assert.notNull(feePaymentForm);
		
		FeePayment result;
		CreditCard creditCard;
		
		result = feePaymentService.create();
		
		creditCard = new CreditCard();
		
		creditCard.setHolderName(feePaymentForm.getHolderName());
		creditCard.setBrandName(feePaymentForm.getBrandName());
		creditCard.setNumber(feePaymentForm.getNumber());
		creditCard.setExpirationMonth(feePaymentForm.getExpirationMonth());
		creditCard.setExpirationYear(feePaymentForm.getExpirationYear());
		creditCard.setCvvCode(feePaymentForm.getCvvCode());
		
		Assert.isTrue(checkCreditCard(creditCard), "Sorry, your credit card has expired. Please enter a valid credit card.");
		
		result.setCreditCard(creditCard);
		result.setAmount(feePaymentForm.getLeague().getAmount());
		result.setPaymentMoment(new Date());
		result.setClub(feePaymentForm.getClub());
		result.setLeague(feePaymentForm.getLeague());
		
		Assert.isTrue(!leagueService.findAllByClubId(result.getClub().getId()).contains(result.getLeague()), "Your club is already registered for this league.");
		
		result = feePaymentService.save(result);
		
		return result;
	}
	
	private boolean checkCreditCard(CreditCard creditCard) {
		boolean result;
		Calendar c;
		int cMonth, cYear;
		
		result = false;
		
		c = Calendar.getInstance();
				
		cMonth = c.get(2) + 1; //Obtenemos numero del mes (Enero es 0)
		cYear = c.get(1); //Obtenemos año
		
		if(creditCard.getExpirationYear() > cYear) {
			result = true;
		} else if(creditCard.getExpirationYear() == cYear) {
			if(creditCard.getExpirationMonth() >= cMonth) {
				result = true;
			}
		}
		return result;		
	}
	
}
