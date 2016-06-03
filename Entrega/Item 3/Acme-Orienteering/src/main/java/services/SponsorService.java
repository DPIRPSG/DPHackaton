package services;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Finances;
import domain.Sponsor;

import repositories.SponsorRepository;

@Service
@Transactional
public class SponsorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private SponsorRepository sponsorRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private FinancesService financesService;
	
	// Constructors -----------------------------------------------------------

	public SponsorService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Sponsor create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create a sponsor");
		
		Sponsor result;
		Collection<Finances> finances;
		
		result = new Sponsor();
		finances = new ArrayList<Finances>();
		
		result.setFinances(finances);
		
		return result;
	}
	
	private Sponsor save(Sponsor input) {
		Assert.notNull(input);
		
		input = sponsorRepository.save(input);
			
		return input;
	}
	
	public Sponsor saveFromEdit(Sponsor input){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can edit sponsors");
		Sponsor result;
		
		if(input.getId() == 0)
			result = this.create();
		else
			result = this.findOne(input.getId());
		
		result.setDescription(input.getDescription());
		result.setLogo(input.getLogo());
		result.setName(input.getName());
		
		input = this.save(result);
		
		return input;
	}
	
	public void delete(Sponsor sponsor) {
		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete sponsors");
		
		financesService.deleteBySponsor(sponsor);
		
		sponsorRepository.delete(sponsor);
	}
	
	public Sponsor findOne(int sponsorId) {
		Sponsor result;
		
		result = sponsorRepository.findOne(sponsorId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Sponsor> findAll() {
		Collection<Sponsor> result;

		result = sponsorRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		sponsorRepository.flush();
	}

}
