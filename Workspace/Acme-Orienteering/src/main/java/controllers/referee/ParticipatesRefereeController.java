package controllers.referee;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Participates;
import services.ParticipatesService;

@Controller
@RequestMapping(value = "/participates/referee")
public class ParticipatesRefereeController extends AbstractController{

	//Services ----------------------------------------------------------
	
	@Autowired
	private ParticipatesService participatesService;
	
	//Constructors ----------------------------------------------------------
	
	public ParticipatesRefereeController(){
		super();
	}

	//Listing ----------------------------------------------------------

	//Creation ----------------------------------------------------------
	
	//Edition ----------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int participatesId){
		ModelAndView result;
		Participates part;
		
		part = participatesService.findOne(participatesId);
		
		result = createEditModelAndView(part, "actorForm.commit.error");
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Participates input, BindingResult binding){
		
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = createEditModelAndView(input);
		} else {
			try {
				participatesService.saveFromClassificationEdit(input);
				result = new ModelAndView("redirect:../..");
				result.addObject("messageStatus", "actorForm.commit.ok");						
			} catch (Throwable oops){
				result = createEditModelAndView(input, "actorForm.commit.error");
			}
		}
		
		return result;
	}
	//Ancillary Methods ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Participates input){
		ModelAndView result;
		
		result = createEditModelAndView(input, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Participates input, String message){
		ModelAndView result;
		
		result = new ModelAndView("participates/editResult");
		result.addObject("participates", input);
		result.addObject("message", message);
		
		return result;
	}
}
