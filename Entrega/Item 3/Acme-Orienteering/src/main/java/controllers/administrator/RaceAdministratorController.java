/* AnnouncementAdministratorController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.LeagueService;
import services.RaceService;
import controllers.AbstractController;
import domain.Category;
import domain.League;
import domain.Race;

@Controller
@RequestMapping("/race/administrator")
public class RaceAdministratorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private RaceService raceService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private CategoryService categoryService;
	
	// Constructors -----------------------------------------------------------
	
	public RaceAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Race> racing;

		racing = raceService.findAll();
		result = new ModelAndView("race/list");
		result.addObject("requestURI", "race/administrator/list.do");
		result.addObject("racing", racing);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Race race;

		race = raceService.create();
		result = createEditModelAndView(race);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int raceId) {
		ModelAndView result;
		Race race;

		race = raceService.findOne(raceId);		
		Assert.notNull(race);
		result = createEditModelAndView(race);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Race race, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(race);
		} else {
			try {
				raceService.save(race);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(race, "race.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Race race, BindingResult binding) {
		ModelAndView result;

		try {			
			raceService.delete(race);
			result = new ModelAndView("redirect:list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(race, "race.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Race race) {
		ModelAndView result;

		result = createEditModelAndView(race, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Race race, String message) {
		ModelAndView result;
		Collection<Category> categories;
		Collection<League> leagues;
		
		categories = categoryService.findAll();
		leagues = leagueService.findAll();
		
		result = new ModelAndView("race/edit");
		result.addObject("race", race);
		result.addObject("categories", categories);
		result.addObject("leagues", leagues);
		result.addObject("message", message);

		return result;
	}

}