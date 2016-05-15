package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EnteredService;
import services.ManagerService;

import controllers.AbstractController;
import domain.Club;
import domain.Entered;
import domain.Manager;

@Controller
@RequestMapping("/entered/manager")
public class EnteredManagerController extends AbstractController{
	
	// Services ---------------------------------------------------------------
	
	@Autowired
	private EnteredService enteredService;
	
	@Autowired
	private ManagerService managerService;
	
	// Constructors -----------------------------------------------------------

	public EnteredManagerController(){
		super();
	}
	
	// Listing ----------------------------------------------------------------

	/**
	 * @see 22.c
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Revisar las distintas peticiones de ingreso que le han hecho al club.
	 * @return the collection of entered received of a club
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(){
		
		ModelAndView result;
		Collection<Entered> entereds;
		Manager manager;
		Club club;
		
		manager = managerService.findByPrincipal();
		club = manager.getClub();
		entereds = enteredService.findAllByClub(club.getId());
		
		result = new ModelAndView("entered/list");
		result.addObject("requestURI", "entered/manager/list.do");
		result.addObject("entereds", entereds);		
		
		return result;
	}

}
