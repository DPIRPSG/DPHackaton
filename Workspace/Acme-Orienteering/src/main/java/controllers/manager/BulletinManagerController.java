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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BulletinService;
import services.ManagerService;
import controllers.AbstractController;
import domain.Bulletin;
import domain.Club;
import domain.Manager;

@Controller
@RequestMapping("/bulletin/manager")
public class BulletinManagerController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private BulletinService bulletinService;
	
	@Autowired
	private ManagerService managerService;
	
	// Constructors -----------------------------------------------------------
	
	public BulletinManagerController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Bulletin> bulletins;
		Club club;
		Manager manager;
		Boolean pertenece;

		manager = managerService.findByPrincipal();
		club = manager.getClub();
		bulletins = club.getBulletins();
		pertenece = true;
		
		result = new ModelAndView("bulletin/list");
		result.addObject("requestURI", "bulletin/manager/list.do");
		result.addObject("requestURI2", "bulletin/manager/create.do");
		result.addObject("bulletins", bulletins);
		result.addObject("club", club);
		result.addObject("pertenece", pertenece);

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
			
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam int bulletinId) {
		ModelAndView result;
		Bulletin bulletin;
		
		bulletin = bulletinService.findOne(bulletinId);
			
		bulletinService.delete(bulletin);
		result = new ModelAndView("redirect:list.do");						

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
		result.addObject("requestURI1", "bulletin/manager/create.do");
		result.addObject("requestURI2", "bulletin/manager/list.do");
		result.addObject("bulletin", bulletin);
		result.addObject("message", message);

		return result;
	}

}