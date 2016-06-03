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
import services.form.ClubFormService;
import services.form.DeleteClubFormService;
import controllers.AbstractController;
import domain.Club;
import domain.Manager;
import domain.form.ClubForm;
import domain.form.DeleteClubForm;

@Controller
@RequestMapping("/club/gerente")
public class ClubManagerController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	@Autowired
	private ClubFormService clubFormService;
	
	@Autowired
	private DeleteClubFormService deleteClubFormService;
	
	@Autowired
	private ManagerService managerService;
	
	// Constructors -----------------------------------------------------------
	
	public ClubManagerController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Club club;
		Manager manager;

		manager = managerService.findByPrincipal();
		club = manager.getClub();
		
		result = new ModelAndView("club/list");
		result.addObject("requestURI", "club/gerente/list.do");
		result.addObject("requestURI2", "bulletin/gerente/list.do");
		result.addObject("clubes", club);
		result.addObject("manager", manager);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ClubForm clubForm;

		clubForm = clubFormService.create();
		result = createEditModelAndView(clubForm);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int clubId) {
		ModelAndView result;
		ClubForm clubForm;

		clubForm = clubFormService.findOne(clubId);		
		Assert.notNull(clubForm);
		clubForm.setClubId(clubId);
		result = createEditModelAndView(clubForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid ClubForm clubForm, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(clubForm);
		} else {
			try {
				Club club;
				
				club = clubFormService.reconstruct(clubForm);
				club = clubService.save(club);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(clubForm, "club.commit.error");				
			}
		}

		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int clubId) {
		ModelAndView result;
		DeleteClubForm clubForm;

		clubForm = deleteClubFormService.findOne(clubId);		
		Assert.notNull(clubForm);
		result = createDeleteModelAndView(clubForm);

		return result;
	}
			
	@RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid DeleteClubForm clubForm, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createDeleteModelAndView(clubForm);
		} else {
			try {
				deleteClubFormService.delete(clubForm);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createDeleteModelAndView(clubForm, "club.commit.error");
			}
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(ClubForm clubForm) {
		ModelAndView result;

		result = createEditModelAndView(clubForm, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(ClubForm clubForm, String message) {
		ModelAndView result;
		
		result = new ModelAndView("club/edit");
		result.addObject("clubForm", clubForm);
		result.addObject("message", message);

		return result;
	}
	
	protected ModelAndView createDeleteModelAndView(DeleteClubForm deleteClubForm) {
		ModelAndView result;

		result = createDeleteModelAndView(deleteClubForm, null);
		
		return result;
	}	
	
	protected ModelAndView createDeleteModelAndView(DeleteClubForm deleteClubForm, String message) {
		ModelAndView result;
		Collection<Manager> managers;
		
		managers = managerService.findAllWithoutClub();
		
		result = new ModelAndView("club/delete");
		result.addObject("deleteClubForm", deleteClubForm);
		result.addObject("message", message);
		result.addObject("managers", managers);

		return result;
	}

}