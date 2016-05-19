package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Club;
import domain.Participates;
import domain.Race;
import domain.Runner;
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
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private RaceService raceService;

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
	
	public void joinRace(int raceId){
		
		/*
		 * Faltan asserts como:
		 * - Que el runner esté en el club.
		 */
		
		Assert.isTrue(actorService.checkAuthority("RUNNER"));
				
		Participates result;
		Runner runner;
		Race race;
		
		Collection<Participates> allParticipatesByRunner;
		Boolean flag;
		
		runner = runnerService.findByPrincipal();
		race = raceService.findOne(raceId);
		
		allParticipatesByRunner = findAllByRunnerIdAndRaceId(runner.getId(), raceId, -1, -1);
		flag = false;
		
		for(Participates p:allParticipatesByRunner){
			if(p.getRace().getId()==raceId){
				flag = true;
				break;
			}
		}
		
		Assert.isTrue(!flag);
		
		result = create();
		result.setRace(race);
		result.setResult(0);
		result.setRunner(runner);
		save(result);
		
	}
	
	/**
	 *  Encuentra por runner y race. En caso de que uno o ambos de los valores sean negativos se ignorará para realizar la búsqueda
	 * @param sponsorId
	 * @param leagueId
	 * @return
	 */
	private Collection<Participates> findAllByRunnerIdAndRaceId(int runnerId, int raceId, int refereeId, int clubId){
		Collection<Participates> result;
		
		result = participatesRepository.findAllByRunnerIdRaceIdAndRefereeId(runnerId, raceId, refereeId, clubId);
				
		return result;
	}
	
	public Collection<Participates> findAllRefereeByRunnerIdAndRaceId(int runnerId, int raceId){
		Assert.isTrue(actorService.checkAuthority("REFEREE"), "findAllRefereeByRunnerIdAndRaceId.permissionDenied");
		
		Collection<Participates> result;
		int refereeId;
		
		refereeId = actorService.findByPrincipal().getId();
	
		result = this.findAllByRunnerIdAndRaceId(runnerId, raceId, refereeId, -1);
		
		return result;
	}
	
	public Collection<Participates> findAllClubByRunnerIdAndRaceId(int runnerId, int raceId){
		boolean res;
		
		res = actorService.checkAuthorities("MANAGER, RUNNER");
		Assert.isTrue(res, "findAllRefereeByRunnerIdAndRaceId.permissionDenied");
		
		Collection<Participates> result;
		int clubId;
		Club club;
		
		if (actorService.checkAuthority("MANAGER"))
			club = managerService.findByPrincipal().getClub();
		else
			club = runnerService.getClub();
				
		clubId = -1;
		if (club != null )
			clubId = club.getId();
		
		result = this.findAllByRunnerIdAndRaceId(runnerId, raceId, -1, clubId);
		
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
