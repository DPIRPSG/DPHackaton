package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	private RunnerService runnerService;
	
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
	
	/**
	 *  Encuentra por club y race. En caso de que uno o ambos de los valores sean negativos se ignorará para realizar la búsqueda
	 * @param sponsorId
	 * @param leagueId
	 * @return
	 */
	public Collection<Classification> findAllByClubIdAndRaceId(int clubId, int raceId){
//		Assert.isTrue(actorService.checkAuthority("REFEREE"), "findAllRefereeByRunnerIdAndRaceId.permissionDenied");
		
		Collection<Classification> result;
	
		result = classificationRepository.findAllByClubIdAndRaceId(clubId, raceId);
		
		return result;
	}
	
	public void calculateClassification(int raceId) {
		Assert.isTrue(actorService.checkAuthority("REFEREE"), "calculateClassification.NotReferee");
		
		Race race;
		Map<Club, Map<String, Integer>> raceClassification;
		List<Integer> clubTotal;
		Integer[] points = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};
		
		race = raceService.findOne(raceId);
		
		Assert.isTrue(actorService.findByPrincipal().getId() == race.getLeague().getReferee().getId(), "calculateClassification.PermissionDenied");
		
		raceClassification = new HashMap<Club, Map<String, Integer>>();
		clubTotal = new ArrayList<Integer>();
		
		for(Participates p:race.getParticipates()){
			Map<String, Integer> clubClassi;
			Club actClub;
			
			Assert.isTrue(p.getResult() > 0, "classification.calculateClassification.runnerWithNoResult");
			
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
			
//			System.out.println("actor: " +p.getRunner().getUserAccount().getUsername() + "; club: " + actClub);
			
			raceClassification.put(actClub, clubClassi);
		}
		
		
		for(Club i:raceClassification.keySet()){
			Map<String, Integer> clubClassi;
			clubClassi = raceClassification.get(i);
			clubTotal.add(clubClassi.get("totalClub") / clubClassi.get("runners"));
		}
//		Comparator<Integer> comparador = Collections.reverseOrder();
//		Collections.sort(clubTotal, comparador);
		Collections.sort(clubTotal);

		for(Club i:raceClassification.keySet()){
			Iterator<Classification> ja2 = this.findAllByClubIdAndRaceId(i.getId(), raceId).iterator();
			Classification actClassi;
			Integer pos;
			Map<String, Integer> clubClassi;

			clubClassi = raceClassification.get(i);
//			System.out.println("i: " + i + "; raceId: " + raceId);

			if(ja2.hasNext()){
				actClassi = ja2.next();
			} else {
				actClassi = this.create();
				
				actClassi.setClub(i);
				actClassi.setRace(race);
			}
			pos = clubTotal.indexOf(clubClassi.get("totalClub") / clubClassi.get("runners"));

			actClassi.setPosition(pos + 1);
			if(pos <= points.length - 1)
				actClassi.setPoints(points[pos]);
			else
				actClassi.setPoints(1);
			
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
