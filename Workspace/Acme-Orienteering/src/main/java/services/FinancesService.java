package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Finances;
import domain.League;
import domain.Sponsor;
import repositories.FinancesRepository;

@Service
@Transactional
public class FinancesService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FinancesRepository financesRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private SponsorService sponsorService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Constructors -----------------------------------------------------------

	public FinancesService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
/**
 * Crea un Finances. En caso de no querer alguno de los parámetros solo es necesario pasarlo con un valor negativo
 * @param sponsorId
 * @param leagueId
 * @return
 */
	public Finances create(int sponsorId, int leagueId) {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create a finances");
		
		Finances result;
		
		result = new Finances();
		result.setPaymentMoment(new Date());
		
		if(sponsorId > 0){
			Sponsor spo;
			
			spo = sponsorService.findOne(sponsorId);
			result.setSponsor(spo);
		}
		if (leagueId > 0){
			League lea;
			
			lea = leagueService.findOne(leagueId);
			result.setLeague(lea);
		}
		
		return result;
	}
	
	private Finances save(Finances input) {
		Assert.notNull(input);
		
		input = financesRepository.save(input);
			
		return input;
	}
	
	public Finances saveFromEdit(Finances input){
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can edit finances");
		Finances result;
		
//		if(input.getId() == 0)
//			result = this.create(-1, -1);
//		else
//			result = this.findOne(input.getId());
//		
//		result.setPaymentMoment(input.getPaymentMoment());
//		result.setAmount(input.getAmount());
		
		result = this.save(input);
		
		return result;
	}
	
	public void delete(Finances input) {
		Assert.notNull(input);
		Assert.isTrue(input.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete finances");
		
		financesRepository.delete(input);
	}
	
	public Finances findOne(int financesId) {
		Finances result;
		
		result = financesRepository.findOne(financesId);
		Assert.notNull(result);
		
		return result;
	}
	
	public Collection<Finances> findAll() {
		Collection<Finances> result;

		result = financesRepository.findAll();

		return result;
	}
	
	/**
	 *  Encuentra por sponsor y liga. En caso de que uno o ambos de los valores sean negativos se ignorará para realizar la búsqueda
	 * @param sponsorId
	 * @param leagueId
	 * @return
	 */
	public Collection<Finances> findBySponsorAndLeagueId(int sponsorId, int leagueId){
		Collection<Finances> result;
		
		if (sponsorId > 0 && leagueId > 0)
			result = financesRepository.findAllBySponsorIdAndLeagueId(sponsorId, leagueId);
		else if (sponsorId > 0)
			result = financesRepository.findAllBySponsorId(sponsorId);
		else if (leagueId > 0)
			result = financesRepository.findAllByLeagueId(leagueId);
		else
			result = this.findAll();
				
		return result;
	}

	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		financesRepository.flush();
	}

}
