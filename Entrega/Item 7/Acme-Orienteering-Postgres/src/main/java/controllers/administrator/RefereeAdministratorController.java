package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.form.ActorForm;

import security.TypeOfAuthority;
import services.form.ActorFormService;

@Controller
@RequestMapping(value = "/referee/administrator")
public class RefereeAdministratorController extends AbstractController{

	//Services ----------------------------------------------------------
	
	@Autowired
	private ActorFormService actorFormService;
	
	@Autowired
	private Validator actorFormValidator;
	
	//Constructors ----------------------------------------------------------
	
	public RefereeAdministratorController(){
		super();
	}

	//Listing ----------------------------------------------------------

	//Creation ----------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(){
		ModelAndView result;
		ActorForm consu;
		
		consu = actorFormService.createForm(TypeOfAuthority.REFEREE);
		result = createEditModelAndView(consu);
		
		return result;
	}
	
	//Edition ----------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid ActorForm input, BindingResult binding){
		actorFormValidator.validate(input, binding);
		
		ModelAndView result;
		
		if(binding.hasErrors()){
			result = createEditModelAndView(input);
		} else {
		try {
				actorFormService.saveForm(input);
				result = new ModelAndView("redirect:../..");
				result.addObject("messageStatus", "actorForm.commit.ok");
												
			} catch (Throwable oops){
				result = createEditModelAndView(input, "actorForm.commit.error");
			}
		}
		
		return result;
	}
	//Ancillary Methods ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(ActorForm customer){
		ModelAndView result;
		
		result = createEditModelAndView(customer, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(ActorForm customer, String message){
		ModelAndView result;
		
		result = new ModelAndView("actorForm/create");
		result.addObject("actorForm", customer);
		result.addObject("message", message);
		result.addObject("urlAction", "referee/administrator/create.do");
		
		return result;
	}
}
