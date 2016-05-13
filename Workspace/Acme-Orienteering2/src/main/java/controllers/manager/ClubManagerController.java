/* AnnouncementAdministratorController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers.manager;

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
import controllers.AbstractController;
import domain.Club;

@Controller
@RequestMapping("/club/manager")
public class ClubManagerController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	// Constructors -----------------------------------------------------------
	
	public ClubManagerController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Club club;

		club = clubService.findClubByManagerId();
		
		result = new ModelAndView("club/list");
		result.addObject("requestURI", "club/manager/list.do");
		result.addObject("clubes", club);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Club club;

		club = clubService.create();
		result = createEditModelAndView(club);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int clubId) {
		ModelAndView result;
		Club club;

		club = clubService.findOne(clubId);		
		Assert.notNull(club);
		result = createEditModelAndView(club);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Club club, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(club);
			System.out.println(binding);
		} else {
			try {
				club = clubService.save(club);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				System.out.println(oops);
				System.out.println(club.getId());
				System.out.println(club.getManager().getId());
				result = createEditModelAndView(club, "club.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Club club, BindingResult binding) {
		ModelAndView result;

		try {			
			clubService.delete(club);
			result = new ModelAndView("redirect:list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(club, "club.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Club club) {
		ModelAndView result;

		result = createEditModelAndView(club, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Club club, String message) {
		ModelAndView result;
		
		result = new ModelAndView("club/edit");
		result.addObject("club", club);
		result.addObject("message", message);

		return result;
	}

}