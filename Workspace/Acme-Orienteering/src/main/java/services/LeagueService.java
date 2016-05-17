package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Comment;
import domain.FeePayment;
import domain.Finances;
import domain.League;
import domain.Punishment;
import domain.Race;
import repositories.LeagueRepository;

@Service
@Transactional
public class LeagueService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private LeagueRepository leagueRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------------------------

	public LeagueService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public League create() {
		Assert.isTrue(actorService.checkAuthority("ADMIN"),
				"Only an administrator can create a league");
		
		League result;
		Collection<String> pictures;
		Collection<Punishment> punishments;
		Collection<FeePayment> feePayments;
		Collection<Race> racing;
		Collection<Finances> finances;
		Collection<Comment> comments;
		
		result = new League();
		pictures = new ArrayList<String>();
		punishments = new ArrayList<Punishment>();
		feePayments = new ArrayList<FeePayment>();
		racing = new ArrayList<Race>();
		finances = new ArrayList<Finances>();
		comments = new ArrayList<Comment>();
		
		result.setPictures(pictures);
		result.setCreationMoment(new Date());
		result.setPunishments(punishments);
		result.setFeePayments(feePayments);
		result.setRacing(racing);
		result.setFinances(finances);
		result.setComments(comments);
		
		return result;
	}
	
	public League save(League league) {
		Assert.notNull(league);
		Assert.isTrue(league.getStartedMoment().compareTo(new Date()) >= 0);

		if(league.getId() == 0) {
			Assert.isTrue(actorService.checkAuthority("ADMIN"),
					"Only an administrator can save a league");
			
			league.setCreationMoment(new Date());
			league = leagueRepository.save(league);
			
		} else {
			league = leagueRepository.save(league);
		}
		return league;
	}
	
	public void delete(League league) {
		Assert.notNull(league);
		Assert.isTrue(league.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("ADMIN"), "Only an admin can delete leagues");
		
		leagueRepository.delete(league);
		
	}
	
	public League findOne(int leagueId) {
		League result;
		
		result = leagueRepository.findOne(leagueId);
		
		return result;
	}
	
	public Collection<League> findAll() {
		Collection<League> result;

		result = leagueRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		leagueRepository.flush();
	}

	public Collection<League> findAllByClubId(Integer clubId) {
		Collection<League> result;
		
		result = leagueRepository.findAllByClubId(clubId);
				
		return result;
	}

}
