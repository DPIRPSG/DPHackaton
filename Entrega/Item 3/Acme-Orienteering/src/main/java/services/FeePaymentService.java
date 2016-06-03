package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FeePaymentRepository;
import domain.Club;
import domain.FeePayment;
import domain.League;

@Service
@Transactional
public class FeePaymentService {
	
	//Managed repository -----------------------------------------------------

	@Autowired
	private FeePaymentRepository feePaymentRepository;
	
	//Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private LeagueService leagueService;

	//Constructors -----------------------------------------------------------
	
	public FeePaymentService(){
		super();
	}
	
	//Simple CRUD methods ----------------------------------------------------
	
	public FeePayment create(){
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can pay for a league.");
		
		FeePayment result;
		
		result = new FeePayment();
		
		return result;
	}
	
	public FeePayment save(FeePayment feePayment){
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can pay for a league.");
		Assert.isTrue(feePayment.getId() == 0, "You cant edit the pay for a league.");
		Assert.notNull(feePayment);
		
		FeePayment result;
		League league;
		Club club;
		Collection<FeePayment> feePaymentsClub, feePaymentsLeague;
		
		result = feePaymentRepository.save(feePayment);
		
		club = result.getClub();
		league = result.getLeague();
		
		feePaymentsClub = club.getFeePayments();
		feePaymentsClub.add(result);
		club.setFeePayments(feePaymentsClub);
		clubService.save(club);
		
		feePaymentsLeague = league.getFeePayments();
		feePaymentsLeague.add(result);
		league.setFeePayments(feePaymentsLeague);
		leagueService.save(league);
		
		return result;
	}
	
	//Other business methods -----------------------------------------------
	
	public void flush() {
		feePaymentRepository.flush();
	}
	
}
