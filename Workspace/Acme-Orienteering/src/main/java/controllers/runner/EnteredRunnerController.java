package controllers.runner;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EnteredService;
import services.RunnerService;

import controllers.AbstractController;
import domain.Entered;
import domain.Runner;

@Controller
@RequestMapping("/entered/runner")
public class EnteredRunnerController extends AbstractController{
	
	// Services ---------------------------------------------------------------
	
	@Autowired
	private EnteredService enteredService;
	
	@Autowired
	private RunnerService runnerService;
	
	// Constructors -----------------------------------------------------------

	public EnteredRunnerController(){
		super();
	}
	
	// Listing ----------------------------------------------------------------

	/**
	 * @see 21.b
	 * 	Un usuario que haya iniciado sesi�n como corredor debe poder:
	 * 	Revisar el estado de las distintas peticiones que haya realizado.
	 * @return the collection of entered filled by a runner
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<Entered> entereds;
		Runner runner;
		
		runner = runnerService.findByPrincipal();
		entereds = enteredService.findAllByRunner(runner.getId());
		
		result = new ModelAndView("entered/list");
		result.addObject("requestURI", "entered/runner/list.do");
		result.addObject("entereds", entereds);		
		
		return result;
	}
	
	// Creation ---------------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int clubId){
		
		ModelAndView result;
		Entered entered;
		
		entered = enteredService.create(clubId);
		result = createEditModelAndView(entered);
		return result;
	}
	
	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Entered entered, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(entered);
		} else {
			try {
				entered = enteredService.save(entered);				
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(entered, "entered.commit.error");				
			}
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Entered entered) {
		ModelAndView result;

		result = createEditModelAndView(entered, null);
		
		return result;
	}	
	
	protected ModelAndView createEditModelAndView(Entered entered, String message) {
		ModelAndView result;
		
		result = new ModelAndView("entered/create");
		result.addObject("entered", entered);
		result.addObject("message", message);

		return result;
	}

}
