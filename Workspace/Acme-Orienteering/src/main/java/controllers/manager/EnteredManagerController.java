package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	 * 	Un usuario que haya iniciado sesi�n como gerente debe poder:
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
	
	/**
	 * 
	 * @param enteredId
	 * @see 22.d
	 * 	Un usuario que haya iniciado sesi�n como gerente debe poder:
	 * 	Aceptar o denegar las peticiones.
	 * @return the view with the entered accepted
	 */
	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView accept(@RequestParam int enteredId){
		
		ModelAndView result;
		Entered entered;
		
		entered = enteredService.findOne(enteredId);
		Assert.notNull(entered);
		
		try{
			enteredService.accept(entered);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.accept.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.accept.error");
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param enteredId
	 * @see 22.d
	 * 	Un usuario que haya iniciado sesi�n como gerente debe poder:
	 * 	Aceptar o denegar las peticiones.
	 * @return the view with the entered accepted
	 */
	@RequestMapping(value = "/deny", method = RequestMethod.GET)
	public ModelAndView deny(@RequestParam int enteredId){
		
		ModelAndView result;
		Entered entered;
		
		entered = enteredService.findOne(enteredId);
		Assert.notNull(entered);
		
		try{
			enteredService.deny(entered);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.deny.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.deny.error");
		}
		
		return result;
	}

}