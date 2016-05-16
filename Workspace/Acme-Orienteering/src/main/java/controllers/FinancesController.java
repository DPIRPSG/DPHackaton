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

import services.FinancesService;
import controllers.AbstractController;
import domain.Finances;

@Controller
@RequestMapping("/finances")
public class FinancesController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private FinancesService financesService;
	
	// Constructors -----------------------------------------------------------
	
	public FinancesController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false, defaultValue = "-1") int sponsorId,
			@RequestParam(required = false, defaultValue = "-1") int leagueId) {
		ModelAndView result;
		Collection<Finances> finances;
		
		finances = financesService.findBySponsorAndLeagueId(sponsorId, leagueId);
		
		result = new ModelAndView("finances/list");
		result.addObject("requestURI", "finances/list.do");
		result.addObject("financess", finances);
		result.addObject("params", "sponsorId=" + sponsorId + "&leagueId=" + leagueId);

		return result;
	}

	// Creation ---------------------------------------------------------------

	// Edition ----------------------------------------------------------------
	
	// Ancillary methods ------------------------------------------------------

}