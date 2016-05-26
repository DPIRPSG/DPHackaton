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

import services.CategoryService;
import services.LeagueService;
import controllers.AbstractController;
import domain.Category;
import domain.League;

@Controller
@RequestMapping("/category/administrator")
public class CategoryAdministratorController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private LeagueService leagueService;
	
	// Constructors -----------------------------------------------------------
	
	public CategoryAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Category> categories;

		categories = categoryService.findAll();
		result = new ModelAndView("category/list");
		result.addObject("requestURI", "category/administrator/list.do");
		result.addObject("categories", categories);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Category category;

		category = categoryService.create();
		result = createEditModelAndView(category);

		return result;
	}

	// Edition ----------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int categoryId) {
		ModelAndView result;
		Category category;

		category = categoryService.findOne(categoryId);		
		Assert.notNull(category);
		result = createEditModelAndView(category);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Category category, BindingResult binding) {
		ModelAndView result;
		Collection<League> leagues;

		if (binding.hasErrors()) {
			result = createEditModelAndView(category);
		} else {
			try {
				categoryService.save(category);	
				
				leagues = leagueService.findAll();
				result = new ModelAndView("league/list");
				result.addObject("requestURI", "league/administrator/list.do");
				result.addObject("leagues", leagues);
				
			} catch (Throwable oops) {
				result = createEditModelAndView(category, "category.commit.error");				
			}
		}

		return result;
	}
			
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Category category, BindingResult binding) {
		ModelAndView result;

		try {			
			categoryService.delete(category);
			result = new ModelAndView("redirect:list.do");						
		} catch (Throwable oops) {
			result = createEditModelAndView(category, "category.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Category category) {
		ModelAndView result;

		result = createEditModelAndView(category, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Category category, String message) {
		ModelAndView result;
		
		result = new ModelAndView("category/edit");
		result.addObject("category", category);
		result.addObject("message", message);

		return result;
	}

}