package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FeePaymentRepository;
import domain.FeePayment;

@Service
@Transactional
public class FeePaymentService {
	
	//Managed repository -----------------------------------------------------

	@Autowired
	private FeePaymentRepository feePaymentRepository;
	
	//Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;

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
		Assert.notNull(feePayment);
		
		FeePayment result;
		
		result = feePaymentRepository.save(feePayment);
		
		return result;
	}
	
	//Other business methods -----------------------------------------------
	
	
	
}
