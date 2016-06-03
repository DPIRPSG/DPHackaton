/* AnnouncementAdministratorController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers.runner;

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
import services.RefereeService;
import services.RunnerService;
import controllers.AbstractController;
import domain.Club;
import domain.Race;
import domain.Runner;

@Controller
@RequestMapping("/race/runner")
public class RaceRunnerController extends AbstractController {
	
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
	
	@Autowired
	private RefereeService refereeService;
	
	// Constructors -----------------------------------------------------------
	
	public RaceRunnerController() {
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

		racing = raceService.findAllByRunnerId(runner.getId());
		
		result = new ModelAndView("race/list");
		result.addObject("requestURI", "race/runner/list.do");
		result.addObject("racing", racing);
		result.addObject("runner", runner);
		result.addObject("club", club);
		
		if(actorService.checkAuthority("REFEREE"))
			result.addObject("referee", refereeService.findByPrincipal());
		
		return result;
	}

}