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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ClubService;
import services.RunnerService;
import controllers.AbstractController;
import domain.Club;
import domain.Runner;

@Controller
@RequestMapping("/club/runner")
public class ClubRunnerController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	@Autowired
	private RunnerService runnerService;
	
	// Constructors -----------------------------------------------------------
	
	public ClubRunnerController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Club club;
		Runner runner;

		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		
		result = new ModelAndView("club/list");
		result.addObject("requestURI", "club/runner/list.do");
		result.addObject("requestURI2", "bulletin/runner/list.do");
		result.addObject("clubes", club);
		//result.addObject("manager", manager);

		return result;
	}

}