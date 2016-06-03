package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

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
import domain.League;
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
	
	@Autowired
	private LeagueService leagueService;
	
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
			manager = managerService.findByPrincipal();
			Assert.isTrue(manager.getClub() == null);
			
			club = clubRepository.save(club);
			
			manager = club.getManager();
			manager.setClub(club);
			managerService.saveFromOthers(manager);
		} else {
			club = clubRepository.save(club);
		}
		
		
			
		return club;
	}
	
	public Club saveFromOthers(Club club) {
		Assert.notNull(club);
		Assert.isTrue(actorService.checkAuthority("RUNNER"));
		
		club = clubRepository.save(club);
		
		return club;
	}
	
	public void delete(Club club, int managerId) {
		Assert.notNull(club);
		Assert.isTrue(club.getId() != 0);
		Assert.isTrue(actorService.checkAuthority("MANAGER"), "Only a manager can delete clubes");
		Manager preSave, postSave;
		
		if(actorService.checkAuthority("MANAGER")) {
			Manager manager;
			
			manager = managerService.findByPrincipal();
			Assert.isTrue(club.getManager().getId() == manager.getId(), "Only the manager of this club can save it");
		}
		
		preSave = managerService.findByPrincipal();
		postSave = managerService.findOne(managerId);
		
		Assert.isTrue(postSave.getClub() == null, "El nuevo Manager no debe ser dueño de un club");
		
		preSave.setClub(null);
		managerService.saveFromOthers(preSave);
		
		club.setManager(postSave);
		club = clubRepository.save(club);
		
		postSave.setClub(club);
		managerService.saveFromOthers(postSave);
		
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
		Collection<ArrayList<Integer>> result, result1;
		Integer points;
		ArrayList<Integer> result2, result3;
		Collection<Club> clubes;
		ArrayList<Integer> introducidos;
		int index;
		
		result = new ArrayList<ArrayList<Integer>>();	
		result1 = new ArrayList<ArrayList<Integer>>();	
		clubes = this.findAllByLeagueId(leagueId);
		index = 0;
		
		int[] arrayPoints = new int[clubes.size()];
		
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
			arrayPoints[index] = points;
			index++;
			result2.add(c.getId());
			result2.add(points);
			result1.add(result2);
		}
		
		ordSelDesc(arrayPoints);
		introducidos = new ArrayList<Integer>();
		
		for(int i = 0; i < arrayPoints.length; i++) {
			result3 = new ArrayList<Integer>();
			for(ArrayList<Integer> list : result1) {
				
				if((list.get(1) == arrayPoints[i]) && !introducidos.contains(list.get(0))) {
					result3.add(i);
					result3.add(list.get(0));
					result3.add(list.get(1));
					result.add(result3);
					introducidos.add(list.get(0));
					break;
				}
			}
		}
		
		return result;
		
	}
	
	public Collection<Club> findAllByRunner(int runnerId){
		Collection<Club> result;
		
		result = clubRepository.findAllByRunner(runnerId);
		
		return result;
	}
	
	private void ordSelDesc(int[] arreglo) {
		for (int i = 0; i < arreglo.length - 1; i++) {
			int max = i;

			for (int j = i + 1; j < arreglo.length; j++) {
				if (arreglo[j] > arreglo[max]) {
					max = j;
				}
			}

			if (i != max) {
				int aux = arreglo[i];
				arreglo[i] = arreglo[max];
				arreglo[max] = aux;
			}
		}
	}
	
	// DASHBOARD
	
	public Collection<Club> findAllWhoHaveWonMoreLeagues(){
		Collection<Club> result = new HashSet<>();
		Collection<League> allLeagues = new HashSet<>();
		Collection<Club> allClubs = new HashSet<>();
		Collection<Club> allWinners = new ArrayList<>();
		Collection<ArrayList<Integer>> ranking = new ArrayList<ArrayList<Integer>>();
		int maxWinner = 0;
		
		allLeagues = leagueService.findAll();
		allClubs = findAll();
		
		for(League l:allLeagues){
			ranking = calculateRankingByLeague(l.getId());
			for(ArrayList<Integer> ai:ranking){
				if(ai.get(0) == 0){
					Club club = findOne(ai.get(1));
					allWinners.add(club);
				}
			}
		}
		
		for(Club c:allClubs){
			int max = 0;
			for(Club c2:allWinners){
				if(c.equals(c2)){
					max++;
				}
			}
			if(max > maxWinner){
				maxWinner = max;
				result.clear();
				result.add(c);
			}else if(max == maxWinner){
				result.add(c);
			}
		}
		
		return result;
	}
	
	public Collection<Club> findAllWhoHaveWonMoreRaces(){
		Collection<Club> result;
		
		result = clubRepository.findAllWhoHaveWonMoreRaces();
		
		return result;
	}
	
	public Collection<Club> findAllWhoHaveMoreDeniedEntered(){
		Collection<Club> result;
		
		result = clubRepository.findAllWhoHaveMoreDeniedEntered();
		
		return result;
	}
	
	public Collection<Club> findAllWhoHaveMorePunishments(){
		Collection<Club> result;
		
		result = clubRepository.findAllWhoHaveMorePunishments();
		
		return result;
	}
	
	public Double ratioOfClubsByLeague(){
		Double result;
		
		result = clubRepository.ratioOfClubsByLeague();
		
		return result;
	}

	public Collection<Club> findAllWithMorePoints(){
		Collection<Club> result;
		
		result = clubRepository.findAllWithMorePoints();
		
		return result;
	}
	
	public Collection<Club> findAllWithLessPoint(){
		Collection<Club> result;
		
		result = clubRepository.findAllWithLessPoint();
		
		return result;
	}
}
