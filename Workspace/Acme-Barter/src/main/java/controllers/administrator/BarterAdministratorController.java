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

import services.BarterService;
import controllers.AbstractController;
import domain.Barter;

@Controller
@RequestMapping("/barter/administrator")
public class BarterAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private BarterService barterService;

	// Constructors -----------------------------------------------------------

	public BarterAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=false, defaultValue="") String keyword) {
		ModelAndView result;
		Collection<Barter> barters;
		String keywordToFind;

		barters = barterService.findAll();
		
		if (!keyword.equals("")) {
			String[] keywordComoArray = keyword.split(" ");
			for (int i = 0; i < keywordComoArray.length; i++) {
				if (!keywordComoArray[i].equals("")) {
					keywordToFind = keywordComoArray[i];
					barters = barterService.findBySingleKeyword(keywordToFind);
					break;
				}
			}
		}
		
		result = new ModelAndView("barter/list");
		result.addObject("requestURI", "barter/administrator/list.do");
		result.addObject("barters", barters);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int barterId) {
		ModelAndView result;
		Barter barter;
		
		barter = barterService.findOne(barterId);
		
		result = createEditModelAndView(barter);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Barter barter, BindingResult binding){

		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(barter);
		} else {
			try {
				barterService.saveToRelate(barter);
				result = new ModelAndView("redirect:list.do?");
			} catch (Throwable oops) {
				barter = barterService.findOne(barter.getId());
				result = createEditModelAndView(barter, "barter.cancel.error");
			}
		}

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
	
	// Closing ---------------------------------------------------------------
	
	@RequestMapping(value = "/close", method = RequestMethod.GET)
	public ModelAndView close(@RequestParam int barterId, @RequestParam int userId) {
		ModelAndView result;
		Barter barter;
		
		barter = barterService.findOne(barterId);

		barterService.close(barter);
		
		result = new ModelAndView("redirect:../listByUser.do?userId=" + userId);

		return result;
	}
	
	// Ancillary Methods
	// ----------------------------------------------------------
	protected ModelAndView createEditModelAndView(Barter barter){
		ModelAndView result;
		
		result = createEditModelAndView(barter, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Barter barter, String message){
		ModelAndView result;
		Collection<Barter> allBarters;

		allBarters = barterService.findAllNotRelated(barter.getId());

		result = new ModelAndView("barter/edit");
		result.addObject("barter", barter);
		result.addObject("message", message);
		result.addObject("allBarters", allBarters);
		
		return result;
	}
}
