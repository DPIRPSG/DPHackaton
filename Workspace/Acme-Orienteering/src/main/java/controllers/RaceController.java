/* AnnouncementAdministratorController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ClubService;
import services.LeagueService;
import services.RaceService;
import services.RunnerService;
import controllers.AbstractController;
import domain.Club;
import domain.Race;
import domain.Runner;

@Controller
@RequestMapping("/race")
public class RaceController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private RaceService raceService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ClubService clubService;
	
	// Constructors -----------------------------------------------------------
	
	public RaceController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer clubId,
			@RequestParam(required = false) Integer leagueId) {
		ModelAndView result;
		Collection<Race> racing;
		Runner runner;
		Club club;
		
		runner = null;
		club = null;
		
		if(actorService.checkAuthority("RUNNER")) {
			runner = runnerService.findByPrincipal();
			club = clubService.findOneByRunnerId(runner.getId());
		}

		if(clubId != null) {
			racing = raceService.findAllByClubId(clubId);
		} else if(leagueId != null) {
			racing = leagueService.findOne(leagueId).getRacing();
		} else {
			racing = raceService.findAll();
		}
		
		result = new ModelAndView("race/list");
		result.addObject("requestURI", "race/list.do");
		result.addObject("racing", racing);
		result.addObject("runner", runner);
		result.addObject("club", club);
		
		return result;
	}

}