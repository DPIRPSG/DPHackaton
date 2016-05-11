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

import services.AttributeDescriptionService;
import controllers.AbstractController;
import domain.AttributeDescription;

@Controller
@RequestMapping("/attribute-description")
public class AttributeDescriptionController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private AttributeDescriptionService attributeDescriptionService;
		
	// Constructors -----------------------------------------------------------
	
	public AttributeDescriptionController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int itemId) {
		ModelAndView result;
		Collection<AttributeDescription> attributesDescription;

		attributesDescription = attributeDescriptionService.findAllByItemId(itemId);
		result = new ModelAndView("attribute-description/list");
		result.addObject("requestURI", "attribute-description/list.do");
		result.addObject("attributesDescription", attributesDescription);
		result.addObject("itemId", itemId);

		return result;
	}
}