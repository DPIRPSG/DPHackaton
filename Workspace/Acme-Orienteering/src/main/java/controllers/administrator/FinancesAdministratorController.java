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

import services.FinancesService;
import services.LeagueService;
import services.SponsorService;
import controllers.AbstractController;
import domain.Finances;
import domain.League;
import domain.Sponsor;

@Controller
@RequestMapping("/finances/administrator")
public class FinancesAdministratorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private FinancesService financesService;
	
	@Autowired
	private SponsorService sponsorService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Constructors -----------------------------------------------------------
	
	public FinancesAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required = false, defaultValue = "-1") int sponsorId,
			@RequestParam(required = false, defaultValue = "-1") int leagueId) {
		ModelAndView result;
		Finances sponsor;

		sponsor = financesService.create(sponsorId, leagueId);
		result = createEditModelAndView(sponsor);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int financesId) {
		ModelAndView result;
		Finances finances;

		finances = financesService.findOne(financesId);		
		Assert.notNull(finances);
		result = createEditModelAndView(finances);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Finances input, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(input);
		} else {
			try {
				financesService.saveFromEdit(input);				
				result = new ModelAndView("redirect:../../finances/list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(input, "finances.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Finances finances, BindingResult binding) {
		ModelAndView result;

		try {			
			financesService.delete(finances);
			result = new ModelAndView("redirect:../../finances/list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(finances, "finances.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Finances input) {
		ModelAndView result;

		result = createEditModelAndView(input, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Finances input, String message) {
		ModelAndView result;
		Collection<Sponsor> sponsors;
		Collection<League> leagues;
		
		sponsors = sponsorService.findAll();
		leagues = leagueService.findAll();
		
		result = new ModelAndView("finances/edit");
		result.addObject("finances", input);
		result.addObject("sponsors", sponsors);
		result.addObject("leagues", leagues);
		result.addObject("message", message);

		return result;
	}

}