package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PunishmentRepository;
import domain.Actor;
import domain.Club;
import domain.League;
import domain.Punishment;

@Service
@Transactional
public class PunishmentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PunishmentRepository punishmentRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Constructors -----------------------------------------------------------

	public PunishmentService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	
	public Punishment create(Integer clubId){
		Assert.isTrue(actorService.checkAuthority("REFEREE"), "Only a referee can manage punishments.");
		
		Punishment result;
		Club club;
		
		club = clubService.findOne(clubId);
		
		result = new Punishment();
		
		result.setClub(club);
		
		return result;
	}
	
	public Punishment save(Punishment punishment){
		Assert.isTrue(actorService.checkAuthority("REFEREE"), "Only a referee can manage punishments.");
		Assert.notNull(punishment);
		
		Punishment result;
		Collection<League> leagues;
		Actor referee;
		League league;
		Club club;
		Collection<Punishment> punishmentsClub, punishmentsLeague;
		
		referee = actorService.findByPrincipal();
		
		leagues = leagueService.findAllByRefereeAndClubId(referee.getId(), punishment.getClub().getId());
		
		Assert.isTrue(leagues.contains(punishment.getLeague()), "You can't sanction a League that is not asigned to you or a League where the club didn't/doesn't participate.");
		
		result = punishmentRepository.save(punishment);
		
		club = result.getClub();
		league = result.getLeague();
		
		punishmentsClub = club.getPunishments();
		punishmentsClub.add(result);
		club.setPunishments(punishmentsClub);
		clubService.save(club);
		
		punishmentsLeague = league.getPunishments();
		punishmentsLeague.add(result);
		league.setPunishments(punishmentsLeague);
		leagueService.save(league);
		
		return result;
	}

	
	public Collection<Punishment> findAll() {
		Collection<Punishment> result;

		result = punishmentRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		punishmentRepository.flush();
	}

	public Collection<Punishment> findAllByClubId(Integer clubId) {
		Collection<Punishment> result;

		result = punishmentRepository.findAllByClubId(clubId);

		return result;
	}
}
