package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LegalTextRepository;
import domain.LegalText;

@Service
@Transactional
public class LegalTextService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private LegalTextRepository legalTextRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------------------------

	public LegalTextService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public LegalText create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create legal texts");
		
		LegalText result;		
		
		result = new LegalText();
		
		return result;
	}
	
	public void save(LegalText legalText) {
		Assert.notNull(legalText);
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can save legal texts");
		
		legalTextRepository.save(legalText);
	}
	
	public void delete(LegalText legalText) {
		Assert.notNull(legalText);
		Assert.isTrue(legalText.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete legal texts");
		
		legalTextRepository.delete(legalText);
		
	}
	
	public LegalText findOne(int legalTextId) {
		LegalText result;
		
		result = legalTextRepository.findOne(legalTextId);
		
		return result;
	}
	
	public Collection<LegalText> findAll() {
		Collection<LegalText> result;

		result = legalTextRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		legalTextRepository.flush();
	}
}
