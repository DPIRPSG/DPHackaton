package controllers.referee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import controllers.AbstractController;

import services.ClassificationService;

@Controller
@RequestMapping(value = "/classification/referee")
public class ClassificationRefereeController extends AbstractController{

	//Services ----------------------------------------------------------
	
	@Autowired
	private ClassificationService classificationService;
	
	//Constructors ----------------------------------------------------------
	
	public ClassificationRefereeController(){
		super();
	}

	//Listing ----------------------------------------------------------
	
	//Creation ----------------------------------------------------------
	
	//Edition ----------------------------------------------------------
	
	@RequestMapping(value = "/calculateClassification", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam int raceId,
			@RequestParam(defaultValue = "classification/list.do") String fromUrl) {
		ModelAndView result;

//		try {
			classificationService.calculateClassification(raceId);
			result = new ModelAndView("redirect:../list.do?raceId=" + raceId);
			result.addObject("messageStatus", "classification.commit.ok");
//		}catch (Throwable oops) {
//			result = new ModelAndView("redirect:../../" + fromUrl);
//			if (oops.getMessage() != null && oops.getMessage().equals("classification.calculateClassification.runnerWithNoResult"))
//				result.addObject("messageStatus", "classification.commit.error.result0");			
//			else
//				result.addObject("messageStatus", "classification.commit.error");
//		}
		
		return result;
	}
	
	//Ancillary Methods ----------------------------------------------------------

}
