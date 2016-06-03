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
import org.springframework.web.servlet.ModelAndView;

import services.SponsorService;
import controllers.AbstractController;
import domain.Sponsor;

@Controller
@RequestMapping("/sponsor")
public class SponsorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private SponsorService sponsorService;
	
	// Constructors -----------------------------------------------------------
	
	public SponsorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Sponsor> sponsors;
		
		sponsors = sponsorService.findAll();
		
		result = new ModelAndView("sponsor/list");
		result.addObject("requestURI", "sponsor/list.do");
		result.addObject("sponsors", sponsors);

		return result;
	}

	// Creation ---------------------------------------------------------------


	// Edition ----------------------------------------------------------------
	
	// Ancillary methods ------------------------------------------------------

}