package controllers.runner;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Participates;
import services.ParticipatesService;

@Controller
@RequestMapping(value = "/participates/runner")
public class ParticipatesRunnerController extends AbstractController{

	//Services ----------------------------------------------------------
	
	@Autowired
	private ParticipatesService participatesService;
		
	//Constructors ----------------------------------------------------------
	
	public ParticipatesRunnerController(){
		super();
	}

	//Listing ----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "-1") int runnerId,
			@RequestParam(defaultValue = "-1") int raceId) {
		ModelAndView result;
		Collection<Participates> parts;
		
		parts = participatesService.findAllClubByRunnerIdAndRaceId(runnerId, raceId);
		
		result = new ModelAndView("participates/list");
		result.addObject("participatess", parts);
		result.addObject("requestURI", "participates/runner/list.do");
		return result;
	}
	
	//Creation ----------------------------------------------------------
	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView join(@RequestParam int raceId){
		ModelAndView result;
		Collection<Participates> parts;
		
		try{
			participatesService.joinRace(raceId);
			
			parts = participatesService.findAllByRaceId(raceId);

			result = new ModelAndView("participates/list");
			result.addObject("participatess", parts);
			result.addObject("requestURI", "participates/runner/list.do");
			
			result.addObject("messageStatus", "participates.join.ok");
		}catch(Throwable oops){
			result = new ModelAndView("redirect:list.do");
			result.addObject("messageStatus", "participates.join.error");
		}
		
		return result;
	}
	
	//Edition ----------------------------------------------------------
	
	//Ancillary Methods ----------------------------------------------------------

}
