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

}
