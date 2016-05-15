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

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SponsorService;
import controllers.AbstractController;
import domain.Sponsor;

@Controller
@RequestMapping("/sponsor/administrator")
public class SponsorAdministratorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private SponsorService sponsorService;
	
	// Constructors -----------------------------------------------------------
	
	public SponsorAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Sponsor sponsor;

		sponsor = sponsorService.create();
		result = createEditModelAndView(sponsor);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int sponsorId) {
		ModelAndView result;
		Sponsor sponsor;

		sponsor = sponsorService.findOne(sponsorId);		
		Assert.notNull(sponsor);
		result = createEditModelAndView(sponsor);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Sponsor input, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			System.out.println(binding.getFieldErrors().toString());
			result = createEditModelAndView(input);
		} else {
			try {
				sponsorService.saveFromEdit(input);				
				result = new ModelAndView("redirect:../../sponsor/list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(input, "sponsor.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Sponsor sponsor, BindingResult binding) {
		ModelAndView result;

		try {			
			sponsorService.delete(sponsor);
			result = new ModelAndView("redirect:../../sponsor/list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(sponsor, "sponsor.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Sponsor input) {
		ModelAndView result;

		result = createEditModelAndView(input, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Sponsor input, String message) {
		ModelAndView result;
		
		result = new ModelAndView("sponsor/edit");
		result.addObject("sponsor", input);
		result.addObject("message", message);

		return result;
	}

}