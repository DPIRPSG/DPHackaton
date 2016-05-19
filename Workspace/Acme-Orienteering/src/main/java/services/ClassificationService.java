package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Classification;
import domain.Club;
import domain.Participates;
import domain.Race;
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
	
	@Autowired
	private RaceService raceService;

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
//	 *  Encuentra por runner y race. En caso de que uno o ambos de los valores sean negativos se ignorar� para realizar la b�squeda
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
	 *  Encuentra por club y race. En caso de que uno o ambos de los valores sean negativos se ignorar� para realizar la b�squeda
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
	
	public void calculateClassification(int raceId) {
		Assert.isTrue(actorService.checkAuthority("REFEREE"), "calculateClassification.NotReferee");
		
		Race race;
		Map<Club, Map<String, Integer>> raceClassification;
		List<Integer> clubTotal;
		
		race = raceService.findOne(raceId);
		
		Assert.isTrue(actorService.findByPrincipal().getId() == race.getLeague().getReferee().getId(), "calculateClassification.PermissionDenied");
		
		raceClassification = new HashMap<Club, Map<String, Integer>>();
		clubTotal = new ArrayList<Integer>();
		
		for(Participates p:race.getParticipates()){
			System.out.println("Resultado de participante '" + p.getRunner().getUserAccount().getUsername() + "' : "+ p.getResult());
			Map<String, Integer> clubClassi;
			Club actClub;
			
			actClub = runnerService.getClub(p.getRunner());
			
			if(raceClassification.containsKey(actClub))
				clubClassi = raceClassification.get(actClub);
			else{
				clubClassi = new HashMap<String, Integer>();
				clubClassi.put("position", 0);
				clubClassi.put("runners", 0);
				clubClassi.put("totalClub", 0);
			}
			
			clubClassi.put("runners", clubClassi.get("runners") + 1);
			clubClassi.put("totalClub", clubClassi.get("totalClub") + p.getResult());
			
			raceClassification.put(actClub, clubClassi);
		}
		
		
		for(Club i:raceClassification.keySet()){
			Map<String, Integer> clubClassi;
			clubClassi = raceClassification.get(i);
			clubTotal.add(clubClassi.get("totalClub") / clubClassi.get("runners"));
		}
		Comparator<Integer> comparador = Collections.reverseOrder();
		Collections.sort(clubTotal, comparador);

		for(Club i:raceClassification.keySet()){
			Iterator<Classification> ja2 = this.findAllByClubIdAndRaceId(i.getId(), raceId).iterator();
			Classification actClassi;
			Integer average;
			Map<String, Integer> clubClassi;

			clubClassi = raceClassification.get(i);

			
			if(ja2.hasNext()){
				actClassi = ja2.next();
			} else {
				actClassi = this.create();
				
				actClassi.setClub(i);
				actClassi.setRace(race);
			}
			average = clubClassi.get("totalClub") / clubClassi.get("runners");

			actClassi.setPosition(clubTotal.indexOf(average) + 1);
			actClassi.setPoints(average);
			System.out.println("Club: '" + actClassi.getClub().getName() + "', Position: '" + actClassi.getPosition() + "', Points: '" + actClassi.getPoints() + "'");
			this.save(actClassi);
		}
	}
	
	
	public Classification findOne(int enteredId){
		Classification result;
		
		result = classificationRepository.findOne(enteredId);
		
		return result;
	}
	
	public void flush(){
		classificationRepository.flush();
	}
}
