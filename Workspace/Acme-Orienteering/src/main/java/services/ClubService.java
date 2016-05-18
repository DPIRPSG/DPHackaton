package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Bulletin;
import domain.Classification;
import domain.Club;
import domain.Comment;
import domain.Entered;
import domain.FeePayment;
import domain.Manager;
import domain.Punishment;
import domain.Referee;
import domain.Runner;

import repositories.ClubRepository;

@Service
@Transactional
public class ClubService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ClubRepository clubRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private RefereeService refereeService;
	
	// Constructors -----------------------------------------------------------

	public ClubService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Club create() {
		Assert.isTrue(actorService.checkAuthority("MANAGER"),
				"Only a manager can create a club");
		
		Club result;
		Manager manager;
		Collection<String> pictures;
		Collection<Bulletin> bulletins;
		Collection<Classification> classifications;
		Collection<Entered> entereds;
		Collection<Punishment> punishments;
		Collection<FeePayment> feePayments;
		Collection<Comment> comments;
		
		result = new Club();
		manager = managerService.findByPrincipal();
		pictures = new ArrayList<String>();
		bulletins = new ArrayList<Bulletin>();
		classifications = new ArrayList<Classification>();
		entereds = new ArrayList<Entered>();
		punishments = new ArrayList<Punishment>();
		feePayments = new ArrayList<FeePayment>();
		comments = new ArrayList<Comment>();
		
		result.setManager(manager);
		result.setPictures(pictures);
		result.setCreationMoment(new Date());
		result.setDeleted(false);
		result.setBulletins(bulletins);
		result.setClassifications(classifications);
		result.setEntered(entereds);
		result.setPunishments(punishments);
		result.setFeePayments(feePayments);
		result.setComments(comments);
		
		return result;
	}
	
	public Club save(Club club) {
		Assert.notNull(club);
		
		Manager manager;
		Runner runner;
		Referee referee;
		Collection<Runner> runners;
		boolean pertenece;
		
		if(actorService.checkAuthority("MANAGER")) {
			manager = managerService.findByPrincipal();
			Assert.isTrue(club.getManager().getId() == manager.getId(), "Only the manager of this club can save it");
		} else if(actorService.checkAuthority("RUNNER")) {
			runner = runnerService.findByPrincipal();
			runners = runnerService.findAllByClubId(club.getId());
			pertenece = false;
			
			for(Runner r : runners) {
				if(runner.getId() == r.getId()) {
					pertenece = true;
					break;
				}
			}
			Assert.isTrue(pertenece, "Only a runner of this club can save it");
		} else if(actorService.checkAuthority("REFEREE")) {
			referee = refereeService.findByPrincipal();
			pertenece = false;
			for(FeePayment f : club.getFeePayments()) {
				if(f.getLeague().getReferee().getId() == referee.getId()) {
					pertenece = true;
					break;
				}
			}
			Assert.isTrue(pertenece, "Only a referee of this club can save it");
		}
		
		if(club.getId() == 0) {
			club = clubRepository.save(club);
			
			manager = club.getManager();
			managerService.saveFromOthers(manager);
		} else {
			club = clubRepository.save(club);
		}
		
		
			
		return club;
	}
	
	public void delete(Club club) {
		Assert.notNull(club);
		Assert.isTrue(club.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can delete clubes");
		
		if(actorService.checkAuthority("MANAGER")) {
			Manager manager;
			
			manager = managerService.findByPrincipal();
			Assert.isTrue(club.getManager().getId() == manager.getId(), "Only the manager of this club can save it");
		}
		
		clubRepository.delete(club);
		
	}
	
	public Club findOne(int clubId) {
		Club result;
		
		result = clubRepository.findOne(clubId);
		
		return result;
	}
	
	public Collection<Club> findAll() {
		Collection<Club> result;

		result = clubRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		clubRepository.flush();
	}

	public Collection<Club> findAllByLeagueId(Integer leagueId) {
		Collection<Club> result;
		
		result = clubRepository.findAllByLeagueId(leagueId);
		
		return result;
	}

	public Club findOneByRunnerId(int id) {
		Club result;
		
		result = clubRepository.findOneByRunnerId(id);
		
		return result;
	}
	
	public Collection<ArrayList<Integer>> calculateRankingByLeague(int leagueId) {
		Collection<ArrayList<Integer>> result;
		Integer points;
		ArrayList<Integer> result2;
		Collection<Club> clubes;
		
		result = new ArrayList<ArrayList<Integer>>();
		
		clubes = this.findAllByLeagueId(leagueId);
		for(Club c : clubes) {
			points = 0;
			result2 = new ArrayList<Integer>();
			for(Classification classification : c.getClassifications()) {
				if(classification.getRace().getLeague().getId() == leagueId) {
					points = points + classification.getPoints();
				}
			}
			for(Punishment p : c.getPunishments()) {
				if(p.getLeague().getId() == leagueId) {
					points = points -p.getPoints();
				}
			}
			result2.add(c.getId());
			result2.add(points);
			result.add(result2);
		}
		
		return result;
		
	}
	
	public Collection<Club> findAllByRunner(int runnerId){
		Collection<Club> result;
		
		result = clubRepository.findAllByRunner(runnerId);
		
		return result;
	}

}
