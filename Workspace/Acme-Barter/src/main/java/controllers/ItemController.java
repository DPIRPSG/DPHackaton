package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import controllers.AbstractController;
import domain.Item;

@Controller
@RequestMapping(value = "/item")
public class ItemController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private ItemService itemService;

	// Constructors ----------------------------------------------------------

	public ItemController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int itemId){
		ModelAndView result;
		Item item;
		
		item = itemService.findOne(itemId);
		
		result = new ModelAndView("item/display");
		result.addObject("item", item);
		
		return result;
	}
}
