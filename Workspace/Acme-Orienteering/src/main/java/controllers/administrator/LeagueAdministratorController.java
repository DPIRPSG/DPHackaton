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

import services.ClubService;
import services.ManagerService;
import services.RefereeService;
import services.form.ClubFormService;
import controllers.AbstractController;
import domain.Club;
import domain.League;
import domain.Manager;
import domain.Referee;
import domain.form.ClubForm;
import domain.form.LeagueForm;

@Controller
@RequestMapping("/league/administrator")
public class LeagueAdministratorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private LeagueFormService leagueFormService;
	
	@Autowired
	private RefereeService refereeService;
	
	// Constructors -----------------------------------------------------------
	
	public LeagueAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<League> leagues;
		
		leagues = leagueService.findAll();
		
		result = new ModelAndView("league/list");
		result.addObject("requestURI", "league/administrator/list.do");
		result.addObject("leagues", leagues);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LeagueForm leagueForm;

		leagueForm = leagueFormService.create();
		result = createEditModelAndView(leagueForm);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int leagueId) {
		ModelAndView result;
		LeagueForm leagueForm;

		leagueForm = leagueFormService.findOne(leagueId);		
		Assert.notNull(leagueForm);
		leagueForm.setLeagueId(leagueId);
		result = createEditModelAndView(leagueForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid LeagueForm leagueForm, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(leagueForm);
		} else {
			try {
				League league;
				
				league = leagueFormService.reconstruct(leagueForm);
				league = leagueService.save(league);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(leagueForm, "league.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(LeagueForm leagueForm, BindingResult binding) {
		ModelAndView result;

		try {			
			leagueFormService.delete(leagueForm);
			result = new ModelAndView("redirect:list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(leagueForm, "league.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(LeagueForm leagueForm) {
		ModelAndView result;

		result = createEditModelAndView(leagueForm, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(LeagueForm leagueForm, String message) {
		ModelAndView result;
		Collection<Referee> referees;
		
		referees = refereeService.findAll();
		
		result = new ModelAndView("league/edit");
		result.addObject("leagueForm", leagueForm);
		result.addObject("message", message);

		return result;
	}

}