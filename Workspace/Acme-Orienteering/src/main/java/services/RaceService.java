package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Classification;
import domain.Comment;
import domain.League;
import domain.Participates;
import domain.Race;

import repositories.RaceRepository;

@Service
@Transactional
public class RaceService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private RaceRepository raceRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Constructors -----------------------------------------------------------

	public RaceService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Race create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an admin can create racing");
		
		Race result;
		Collection<Classification> classifications;
		Collection<Participates> participates;
		Collection<Comment> comments;
		
		result = new Race();
		classifications = new ArrayList<Classification>();
		participates = new ArrayList<Participates>();
		comments = new ArrayList<Comment>();
		
		result.setClassifications(classifications);
		result.setParticipates(participates);
		result.setComments(comments);
		
		return result;
	}
	
	public Race save(Race race) {
		Assert.notNull(race);
		League league;
		Race preSave;
		
		if(race.getId() == 0) {
			Assert.isTrue(actorService.checkAuthority("ADMIN"),
					"Only an admin can save racing");
			Assert.isTrue(race.getMoment().compareTo(new Date()) > 0, "La fecha de una carrera debe ser en el futuro");
			
			race = raceRepository.save(race);
			
			league = race.getLeague();
			
			league.addRace(race);
			
			leagueService.save(league);
		} else {
			preSave = this.findOne(race.getId());
			
			league = preSave.getLeague();
			league.removeRace(preSave);
			leagueService.save(league);
			
			preSave.setName(race.getName());
			preSave.setDescription(race.getDescription());
			preSave.setMoment(race.getMoment());
			preSave.setCategory(race.getCategory());
			preSave.setLeague(race.getLeague());
			
			race = raceRepository.save(preSave);
			
			league = race.getLeague();
			league.addRace(race);
			leagueService.save(league);
		}
		
		return race;
	}
	
	public void delete(Race race) {
		Assert.notNull(race);
		Assert.isTrue(race.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete racing");
		
		League league;
		
		league = race.getLeague();
		league.removeRace(race);
		leagueService.save(league);
		
		raceRepository.delete(race);
		
	}
	
	public Race findOne(int raceId) {
		Race result;
		
		result = raceRepository.findOne(raceId);
		
		return result;
	}
	
	public Collection<Race> findAll() {
		Collection<Race> result;

		result = raceRepository.findAll();

		return result;
	}
		

	// Other business methods -------------------------------------------------
	
	public void flush() {
		raceRepository.flush();
	}

	public Collection<Race> findAllByClubId(Integer clubId) {
		Collection<Race> result;
		
		result = raceRepository.findAllByClubId(clubId);
		
		return result;
	}
	
	public Collection<Race> findAllByRunnerId(int runnerId){
		Collection<Race> result;
		
		result = raceRepository.findAllByRunnerId(runnerId);
		
		return result;
	}

	// DASHBOARD
	
	public Double ratioOfRacesByLeague(){
		Double result;
		
		result = raceRepository.ratioOfRacesByLeague();
		
		return result;
	}
	
}
