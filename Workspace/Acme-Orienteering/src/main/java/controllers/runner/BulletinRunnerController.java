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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BulletinService;
import services.ClubService;
import services.RunnerService;
import controllers.AbstractController;
import domain.Bulletin;
import domain.Club;
import domain.Runner;

@Controller
@RequestMapping("/bulletin/runner")
public class BulletinRunnerController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private BulletinService bulletinService;
	
	@Autowired
	private RunnerService runnerService;
	
	@Autowired
	private ClubService clubService;
	
	// Constructors -----------------------------------------------------------
	
	public BulletinRunnerController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Bulletin> bulletins;
		Club club;
		Runner runner;

		runner = runnerService.findByPrincipal();
		club = clubService.findOneByRunnerId(runner.getId());
		bulletins = club.getBulletins();
		
		result = new ModelAndView("bulletin/list");
		result.addObject("requestURI", "bulletin/runner/list.do");
		result.addObject("requestURI2", "bulletin/runner/create.do");
		result.addObject("bulletins", bulletins);
		result.addObject("club", club);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Bulletin bulletin;

		bulletin = bulletinService.create();
		result = createEditModelAndView(bulletin);

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Bulletin bulletin, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(bulletin);
		} else {
			try {
				bulletin = bulletinService.save(bulletin);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(bulletin, "bulletin.commit.error");				
			}
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Bulletin bulletin) {
		ModelAndView result;

		result = createEditModelAndView(bulletin, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Bulletin bulletin, String message) {
		ModelAndView result;
		
		result = new ModelAndView("bulletin/create");
		result.addObject("requestURI1", "bulletin/runner/create.do");
		result.addObject("requestURI2", "bulletin/runner/list.do");
		result.addObject("bulletin", bulletin);
		result.addObject("message", message);

		return result;
	}

}