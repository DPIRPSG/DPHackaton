package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Classification;
import domain.Club;
import domain.Participates;
import repositories.ClassificationRepository;
import repositories.ParticipatesRepository;

@Service
@Transactional
public class ClassificationService {
	
	// Managed repository -----------------------------------------------------
	
	@Autowired
	private ClassificationRepository classificationRepository;
	
	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RefereeService refereeService;
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ManagerService managerService;

	// Constructors -----------------------------------------------------------
	
	public ClassificationService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public Classification create(){		
		Classification result;
		
		result = new Classification();
		
		return result;
	}
	
	private Classification save(Classification participates){
		Assert.notNull(participates);
		
		classificationRepository.save(participates);
		
		return participates;
	}
	
//	public Classification saveFromClassificationEdit(Classification participates){
//		Assert.isTrue(actorService.checkAuthority("REFEREE"), "participates.saveFromClassificationEdit.permissionDenied");
//		
//		Participates result;
//		
//		result = classificationRepository.findOne(participates.getId());
//		
//		Assert.isTrue(result.getRace().getLeague().getReferee().equals(refereeService.findByPrincipal()),
//				"participates.saveFromClassificationEdit.notOwner");
//		
//		result.setResult(participates.getResult());
//		result = this.save(result);
//		
//		return result;
//	}
//	
//	/**
//	 *  Encuentra por runner y race. En caso de que uno o ambos de los valores sean negativos se ignorará para realizar la búsqueda
//	 * @param sponsorId
//	 * @param leagueId
//	 * @return
//	 */
//	private Collection<Participates> findAllByRunnerIdAndRaceId(int runnerId, int raceId, int refereeId, int clubId){
//		Collection<Participates> result;
//		
//		result = classificationRepository.findAllByRunnerIdRaceIdAndRefereeId(runnerId, raceId, refereeId, clubId);
//				
//		return result;
//	}
	
	/**
	 *  Encuentra por club y race. En caso de que uno o ambos de los valores sean negativos se ignorará para realizar la búsqueda
	 * @param sponsorId
	 * @param leagueId
	 * @return
	 */
	public Collection<Classification> findAllByClubIdAndRaceId(int clubId, int raceId){
		// Assert.isTrue(actorService.checkAuthority("REFEREE"), "findAllRefereeByRunnerIdAndRaceId.permissionDenied");
		
		Collection<Classification> result;
	
		result = classificationRepository.findAllByClubIdAndRaceId(clubId, raceId);
		
		return result;
	}
//	
//	public Collection<Participates> findAllClubByRunnerIdAndRaceId(int runnerId, int raceId){
//		boolean res;
//		
//		res = actorService.checkAuthorities("MANAGER, RUNNER");
//		Assert.isTrue(res, "findAllRefereeByRunnerIdAndRaceId.permissionDenied");
//		
//		Collection<Participates> result;
//		int clubId;
//		Club club;
//		
//		if (actorService.checkAuthority("MANAGER"))
//			club = managerService.findByPrincipal().getClub();
//		else
//			club = runnerService.getClub();
//				
//		clubId = -1;
//		if (club != null )
//			clubId = club.getId();
//		
//		result = this.findAllByRunnerIdAndRaceId(runnerId, raceId, -1, clubId);
//		
//		return result;
//	}
	
	
	public Classification findOne(int enteredId){
		Classification result;
		
		result = classificationRepository.findOne(enteredId);
		
		return result;
	}
	
	public void flush(){
		classificationRepository.flush();
	}
}
