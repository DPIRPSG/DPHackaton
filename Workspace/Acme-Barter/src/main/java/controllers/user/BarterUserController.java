package controllers.user;

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

import controllers.AbstractController;

import services.BarterService;
import services.UserService;
import services.form.BarterFormService;
import domain.Barter;
import domain.form.BarterForm;

@Controller
@RequestMapping("/barter/user")
public class BarterUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private BarterService barterService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BarterFormService barterFormService;

	// Constructors -----------------------------------------------------------

	public BarterUserController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="") String keyword) {
		ModelAndView result;
		Collection<Barter> barters;
		String keywordToFind;

		barters = barterService.findAllNotCancelled();
		
		if (!keyword.equals("")) {
			String[] keywordComoArray = keyword.split(" ");
			for (int i = 0; i < keywordComoArray.length; i++) {
				if (!keywordComoArray[i].equals("")) {
					keywordToFind = keywordComoArray[i];
					barters = barterService.findBySingleKeywordNotCancelled(keywordToFind);
					break;
				}
			}
		}
		
		result = new ModelAndView("barter/list");
		result.addObject("requestURI", "barter/user/list.do");
		result.addObject("barters", barters);
		result.addObject("userId", userService.findByPrincipal().getId());

		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		BarterForm barterForm;
		
		barterForm = barterFormService.create();
		result = createEditModelAndView(barterForm);
		
		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid BarterForm barterForm, BindingResult binding){
		ModelAndView result;
		Barter barter;
				
		if (binding.hasErrors()) {
			result = createEditModelAndView(barterForm);
		} else {
			try {
				barter = barterFormService.reconstruct(barterForm);
				barterService.saveToEdit(barter);
				result = new ModelAndView("redirect:list.do?");
			} catch (Throwable oops) {
				System.out.println(oops);
				result = createEditModelAndView(barterForm, "barter.cancel.error");
			}
		}

		return result;
	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Collection<Barter> barters;

		barters = barterService.findAllByFollowedUser();
		
		result = new ModelAndView("barter/display");
		result.addObject("requestURI", "barter/user/display.do");
		result.addObject("barters", barters);

		return result;
	}
	
	@RequestMapping(value="/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam int barterId){
		
		ModelAndView result;
		Barter barter;
		
		barter = barterService.findOne(barterId);
		Assert.notNull(barter);
		
		try{
			barterService.cancel(barter);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "barter.cancel.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "barter.cancel.error");
		}
		
		return result;
	}
	
	// Ancillary Methods
	// ----------------------------------------------------------
	protected ModelAndView createEditModelAndView(BarterForm barterForm){
		ModelAndView result;
		
		result = createEditModelAndView(barterForm, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(BarterForm barterForm, String message){
		ModelAndView result;		
		
		result = new ModelAndView("barter/create");
		result.addObject("barterForm", barterForm);
		result.addObject("message", message);
		
		return result;
	}
}
