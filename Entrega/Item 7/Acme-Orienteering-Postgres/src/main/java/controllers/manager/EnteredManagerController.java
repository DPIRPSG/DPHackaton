package controllers.manager;

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

import services.EnteredService;
import services.ManagerService;

import controllers.AbstractController;
import domain.Club;
import domain.Entered;
import domain.Manager;

@Controller
@RequestMapping("/entered/gerente")
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
		if(club != null) {
			entereds = enteredService.findAllByClub(club.getId());
		} else {
			entereds = null;
		}
		
		
		result = new ModelAndView("entered/list");
		result.addObject("requestURI", "entered/gerente/list.do");
		result.addObject("entereds", entereds);		
		
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int enteredId) {
		ModelAndView result;
		Entered entered;

		entered = enteredService.findOne(enteredId);
		Assert.notNull(entered);
		result = createEditModelAndView(entered);

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Entered entered, BindingResult binding) {
		ModelAndView result;
		String report;
		Entered enteredPreSave;
		
		report = entered.getReport();
		enteredPreSave = enteredService.findOne(entered.getId());
		enteredPreSave.setReport(report);
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(entered);
		} else {
			try {
				enteredService.save(enteredPreSave);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(entered, "race.commit.error");				
			}
		}

		return result;
	}
	
	/**
	 * 
	 * @param enteredId
	 * @see 22.d
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
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
			result.addObject("messageStatus", "entered.commit.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.commit.error");
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param enteredId
	 * @see 22.d
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
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
			result.addObject("messageStatus", "entered.commit.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.commit.error");
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param enteredId
	 * @see 22.
	 * 	Un usuario que haya iniciado sesión como gerente debe poder:
	 * 	Expulsar a un corredor de su club.
	 * @return the view with the entered accepted
	 */
	@RequestMapping(value = "/expel", method = RequestMethod.GET)
	public ModelAndView expel(@RequestParam int enteredId){
		
		ModelAndView result;
		Entered entered;
		
		entered = enteredService.findOne(enteredId);
		Assert.notNull(entered);
		
		try{
			enteredService.expel(entered);
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.commit.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "entered.commit.error");
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
		
		result = new ModelAndView("entered/edit");
		result.addObject("entered", entered);
		result.addObject("message", message);

		return result;
	}

}
