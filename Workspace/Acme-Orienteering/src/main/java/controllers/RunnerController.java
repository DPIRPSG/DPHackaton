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

import services.RunnerService;
import controllers.AbstractController;
import domain.Runner;

@Controller
@RequestMapping("/runner")
public class RunnerController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private RunnerService runnerService;
	
	// Constructors -----------------------------------------------------------
	
	public RunnerController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int clubId) {
		ModelAndView result;
		Collection<Runner> runners;

		runners = runnerService.findAllByClubId(clubId);
		
		result = new ModelAndView("runner/list");
		result.addObject("requestURI", "runner/list.do");
		result.addObject("runners", runners);

		return result;
	}

	// Creation ---------------------------------------------------------------

}