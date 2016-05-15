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

import services.PunishmentService;
import controllers.AbstractController;
import domain.Punishment;

@Controller
@RequestMapping("/punishment")
public class PunishmentController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private PunishmentService punishmentService;
	
	// Constructors -----------------------------------------------------------
	
	public PunishmentController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam (required = false) Integer clubId) {
		ModelAndView result;
		Collection<Punishment> punishments;

		if(clubId != null) {
			punishments = punishmentService.findAllByClubId(clubId);
		} else {
			punishments = punishmentService.findAll();
		}
		
		result = new ModelAndView("punishment/list");
		result.addObject("requestURI", "punishment/list.do");
		result.addObject("punishments", punishments);

		return result;
	}

	// Creation ---------------------------------------------------------------

}