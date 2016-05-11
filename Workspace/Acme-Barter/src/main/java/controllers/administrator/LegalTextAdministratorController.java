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

import services.LegalTextService;

import controllers.AbstractController;
import domain.LegalText;

@Controller
@RequestMapping("/legal-text/administrator")
public class LegalTextAdministratorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private LegalTextService legalTextService;
	
	// Constructors -----------------------------------------------------------
	
	public LegalTextAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<LegalText> legalTexts;

		legalTexts = legalTextService.findAll();
		result = new ModelAndView("legal-text/list");
		result.addObject("requestURI", "legal-text/administrator/list.do");
		result.addObject("legalTexts", legalTexts);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LegalText legalText;

		legalText = legalTextService.create();
		result = createEditModelAndView(legalText);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int legalTextId) {
		ModelAndView result;
		LegalText legalText;

		legalText = legalTextService.findOne(legalTextId);		
		Assert.notNull(legalText);
		result = createEditModelAndView(legalText);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid LegalText legalText, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(legalText);
		} else {
			try {
				legalTextService.save(legalText);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(legalText, "legalText.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(LegalText legalText, BindingResult binding) {
		ModelAndView result;

		try {			
			legalTextService.delete(legalText);
			result = new ModelAndView("redirect:list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(legalText, "legalText.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(LegalText legalText) {
		ModelAndView result;

		result = createEditModelAndView(legalText, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(LegalText legalText, String message) {
		ModelAndView result;
		
		result = new ModelAndView("legal-text/edit");
		result.addObject("legalText", legalText);
		result.addObject("message", message);

		return result;
	}

}