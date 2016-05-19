package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import domain.Classification;
import services.ClassificationService;

@Controller
@RequestMapping(value = "/classification")
public class ClassificationController extends AbstractController{

	//Services ----------------------------------------------------------
	
	@Autowired
	private ClassificationService classificationService;
	
	//Constructors ----------------------------------------------------------
	
	public ClassificationController(){
		super();
	}

	//Listing ----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "-1") int clubId,
			@RequestParam(defaultValue = "-1") int raceId) {
		ModelAndView result;
		Collection<Classification> parts;
		
		parts = classificationService.findAllByClubIdAndRaceId(clubId, raceId);
		
		result = new ModelAndView("classification/list");
		result.addObject("classifications", parts);
		result.addObject("requestURI", "classification/list.do");
		return result;
	}
	
	//Creation ----------------------------------------------------------
	
	//Edition ----------------------------------------------------------
	
	//Ancillary Methods ----------------------------------------------------------

}
