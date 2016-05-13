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
		Assert.isTrue(actorService.checkAuthority("MANAGER"),
				"Only a manager can save clubes");
		System.out.println("save1");
		
		if(club.getId() == 0){
			Manager manager;
			Collection<Bulletin> bulletins;
			Collection<Classification> classifications;
			Collection<Entered> entereds;
			Collection<Punishment> punishments;
			Collection<FeePayment> feePayments;
			Collection<Comment> comments;
			
			manager = managerService.findByPrincipal();
			bulletins = new ArrayList<Bulletin>();
			classifications = new ArrayList<Classification>();
			entereds = new ArrayList<Entered>();
			punishments = new ArrayList<Punishment>();
			feePayments = new ArrayList<FeePayment>();
			comments = new ArrayList<Comment>();
			
			club.setManager(manager);
			club.setCreationMoment(new Date());
			club.setDeleted(false);
			club.setBulletins(bulletins);
			club.setClassifications(classifications);
			club.setEntered(entereds);
			club.setPunishments(punishments);
			club.setFeePayments(feePayments);
			club.setComments(comments);
			
			System.out.println("save2");
			club = clubRepository.save(club);
			
			manager.setClub(club);
			
			System.out.println("save3");
			managerService.save(manager);
		}
		
		System.out.println("save4");
		return club;
	}
	
	public void delete(Club club) {
		Assert.notNull(club);
		Assert.isTrue(club.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can delete clubes");
		
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

	public Club findClubByManagerId() {
		Club result;
		Manager manager;
		
		manager = managerService.findByPrincipal();
		
		result = clubRepository.findByManagerId(manager.getId());
		
		return result;
	}
}
