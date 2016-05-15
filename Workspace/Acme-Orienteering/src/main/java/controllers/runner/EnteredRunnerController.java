package controllers.runner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	 * 	Un usuario que haya iniciado sesión como corredor debe poder:
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

}
