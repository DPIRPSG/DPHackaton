package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Item;

import services.ItemService;

@Controller
@RequestMapping("/item/user")
public class ItemUserController extends AbstractController{

	// Services ---------------------------------------------------------------

	@Autowired
	private ItemService itemService;
	
	// Constructors -----------------------------------------------------------

	public ItemUserController(){
		super();
	}
	
	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Item> items;
		
		items = itemService.findAllByUser();		

		result = new ModelAndView("item/list");
		result.addObject("requestURI", "item/user/list.do");
		result.addObject("items", items);

		return result;
	}

	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(required=false) Integer item1Id){
		ModelAndView result;
		Item item;
		
		item = itemService.create();
		result = createEditModelAndViewCreate(item);
		
		/* El item1Id funciona bien */
		
		if(item1Id != null){
			result.addObject("item1Id", item1Id);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int itemId, @RequestParam(value="item1Id", required = false) Integer item1Id){
		ModelAndView result;
		Item item;
		
		item = itemService.findOne(itemId);
		
		result = createEditModelAndViewEdit(item);
				
		if(item1Id != null){
			result.addObject("item1Id", item1Id);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Item item, BindingResult binding, @RequestParam(value="item1Id",  required = false) Integer item1Id){
		ModelAndView result;
		Collection<Item> before;
		Collection<Item> after;
		Item newItem;
		
		System.out.println(item1Id);
				
		if(binding.hasErrors()){
			result = createEditModelAndViewEdit(item);
		}else{
			try{
				if(item1Id != null){
					before = itemService.findAll();
					itemService.save(item);
					after = itemService.findAll();
					after.removeAll(before);
					newItem = after.iterator().next();
					result = new ModelAndView("redirect:create.do");
					result.addObject("item1Id", item1Id);
					result.addObject("item2Id", newItem.getId());
					System.out.println(item1Id);
					System.out.println(newItem.getId());
				}else{
					
				before = itemService.findAll();
				itemService.save(item);
				after = itemService.findAll();
				after.removeAll(before);
				newItem = after.iterator().next();
				result = new ModelAndView("redirect:create.do");
				result.addObject("item1Id", newItem.getId());
				System.out.println(newItem.getId());
				}
				
			}catch(Throwable oops){
				result = createEditModelAndViewEdit(item, "item.cancel.error");
			}
		}
		
		return result;
	}
	
	// Ancillary Methods
	// ----------------------------------------------------------
	protected ModelAndView createEditModelAndViewCreate(Item item){
		ModelAndView result;
		
		result = createEditModelAndViewCreate(item, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewCreate(Item item, String message){
		ModelAndView result;		
		
		result = new ModelAndView("item/create");
		result.addObject("item", item);
		result.addObject("message", message);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewEdit(Item item){
		ModelAndView result;
		
		result = createEditModelAndViewEdit(item, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndViewEdit(Item item, String message){
		ModelAndView result;		
		
		result = new ModelAndView("item/create");
		result.addObject("item", item);
		result.addObject("message", message);
		
		return result;
	}
}
