package services.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.League;
import domain.Referee;
import domain.form.LeagueForm;

import services.LeagueService;
import services.RefereeService;

@Service
@Transactional
public class LeagueFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private RefereeService refereeService;
	
	// Constructors -----------------------------------------------------------

	public LeagueFormService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public LeagueForm create() {
		LeagueForm result;
		Collection<String> pictures;
		
		pictures = new ArrayList<String>();
		
		result = new LeagueForm();
		
		result.setPictures(pictures);
		result.setLeagueId(0);
		
		return result;
	}
	
	public League reconstruct(LeagueForm leagueForm) {
		League result;
		Referee referee;
		
		referee = refereeService.findOne(leagueForm.getRefereeId());
		
		if (leagueForm.getLeagueId() == 0) {
			result = leagueService.create();
			result.setName(leagueForm.getName());
			result.setDescription(leagueForm.getDescription());
			result.setPictures(leagueForm.getPictures());
			result.setCreationMoment(new Date());
			result.setStartedMoment(leagueForm.getStartedMoment());
			result.setAmount(leagueForm.getAmount());
			result.setReferee(referee);
		} else if(leagueForm.getLeagueId() != 0) {			
			result = leagueService.findOne(leagueForm.getLeagueId());
			result.setName(leagueForm.getName());
			result.setDescription(leagueForm.getDescription());
			result.setPictures(leagueForm.getPictures());
			result.setStartedMoment(leagueForm.getStartedMoment());
			result.setAmount(leagueForm.getAmount());
			result.setReferee(referee);
		} else {
			result = null;
		}
		
		return result;
	}

	public LeagueForm findOne(int leagueId) {
		LeagueForm result;
		League league;
		
		result = this.create();
		league = leagueService.findOne(leagueId);
		result.setName(league.getName());
		result.setDescription(league.getDescription());
		result.setPictures(league.getPictures());
		result.setStartedMoment(league.getStartedMoment());
		result.setAmount(league.getAmount());
		result.setRefereeId(league.getReferee().getId());
		
		return result;
	}

	public void delete(LeagueForm leagueForm) {
		League league;
		league = leagueService.findOne(leagueForm.getLeagueId());
		leagueService.delete(league);
	}
}
