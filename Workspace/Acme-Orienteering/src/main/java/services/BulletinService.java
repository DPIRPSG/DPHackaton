package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Bulletin;
import domain.Club;
import domain.Entered;
import domain.Manager;
import domain.Runner;

import repositories.BulletinRepository;

@Service
@Transactional
public class BulletinService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private BulletinRepository bulletinRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private RunnerService runnerService;
	
	// Constructors -----------------------------------------------------------

	public BulletinService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Bulletin create() {
		Assert.isTrue(actorService.checkAuthority("MANAGER") || actorService.checkAuthority("RUNNER"),
				"Only a manager or a runner can create a Bulletin");
		Bulletin result;
		Club club;
		
		club = null;
		
		if(actorService.checkAuthority("MANAGER")) {
			Manager manager;
			
			manager = managerService.findByPrincipal();
			club = manager.getClub();
		}
		
		if(actorService.checkAuthority("RUNNER")) {
			Runner runner;
			
			runner = runnerService.findByPrincipal();
			for(Entered entered : runner.getEntered()) {
				if(entered.getIsMember() == true) {
					club = entered.getClub();
					break;
				}
			}
		}
		
		result = new Bulletin();
		result.setClub(club);
		result.setCreationMoment(new Date());
		
		return result;
	}
	
	public Bulletin save(Bulletin bulletin) {
		Assert.notNull(bulletin);
		Assert.isTrue(actorService.checkAuthority("MANAGER") || actorService.checkAuthority("RUNNER"),
				"Only a manager or a runner can save a Bulletin");
		Club club;
		
		club = null;
		
		if(actorService.checkAuthority("MANAGER")) {
			Manager manager;
			
			manager = managerService.findByPrincipal();
			club = manager.getClub();
		}
		
		if(actorService.checkAuthority("RUNNER")) {
			Runner runner;
			
			runner = runnerService.findByPrincipal();
			for(Entered entered : runner.getEntered()) {
				if(entered.getIsMember() == true) {
					club = entered.getClub();
					break;
				}
			}
		}
		
		bulletin.setClub(club);
		bulletin.setCreationMoment(new Date());
		
		bulletin = bulletinRepository.save(bulletin);
		
		club.addBulletin(bulletin);
		
		clubService.save(club);
		
		return bulletin;
	}
	
	public void delete(Bulletin bulletin) {
		Assert.notNull(bulletin);
		Assert.isTrue(bulletin.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can delete bulletins");
		
		Manager manager;
		Club club;
			
		manager = managerService.findByPrincipal();
		Assert.isTrue(bulletin.getClub().getManager().getId() == manager.getId(), "Only the manager of this club can delete bulletins");
		
		club = bulletin.getClub();
		club.removeBulletin(bulletin);
		clubService.save(club);
		
		bulletinRepository.delete(bulletin);
		
	}
	
	public Bulletin findOne(int bulletinId) {
		Bulletin result;
		
		result = bulletinRepository.findOne(bulletinId);
		
		return result;
	}
	
	public Collection<Bulletin> findAllByClubId(int clubId) {
		Collection<Bulletin> result;

		result = bulletinRepository.findAllByClubId(clubId);

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		bulletinRepository.flush();
	}
	
}
