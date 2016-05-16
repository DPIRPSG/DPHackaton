package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Participates;
import repositories.ParticipatesRepository;

@Service
@Transactional
public class ParticipatesService {
	
	// Managed repository -----------------------------------------------------
	
	@Autowired
	private ParticipatesRepository participatesRepository;
	
	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RefereeService refereeService;

	// Constructors -----------------------------------------------------------
	
	public ParticipatesService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public Participates create(){		
		Participates result;
		
		result = new Participates();
		
		return result;
	}
	
	private Participates save(Participates participates){
		Assert.notNull(participates);
		
		participatesRepository.save(participates);
		
		return participates;
	}
	
	public Participates saveFromClassificationEdit(Participates participates){
		Assert.isTrue(actorService.checkAuthority("REFEREE"), "participates.saveFromClassificationEdit.permissionDenied");
		
		Participates result;
		
		result = participatesRepository.findOne(participates.getId());
		
		Assert.isTrue(result.getRace().getLeague().getReferee().equals(refereeService.findByPrincipal()),
				"participates.saveFromClassificationEdit.notOwner");
		
		result.setResult(participates.getResult());
		result = this.save(result);
		
		return result;
	}
	
	
	public Participates findOne(int enteredId){
		Assert.isTrue(actorService.checkLogin(), "Only the loged users can find participates");
		Participates result;
		
		result = participatesRepository.findOne(enteredId);
		
		return result;
	}
	
	public void flush(){
		participatesRepository.flush();
	}
}
