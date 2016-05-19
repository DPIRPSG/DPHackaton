package controllers.referee;

import java.util.Collection;

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
import services.ClassificationService;
import services.ParticipatesService;

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
	public ModelAndView create(@RequestParam int raceId, @RequestParam(defaultValue = "classification/list.do") String requestUri){
		ModelAndView result;

		try {
			classificationService.calculateClassification(raceId);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:../../" + requestUri);
			result.addObject("messageStatus", "classification.commit.error");
		} finally {
			result = new ModelAndView("redirect:../../" + requestUri);
			result.addObject("messageStatus", "classification.commit.ok");
		}
		
		return result;
	}
	
	//Ancillary Methods ----------------------------------------------------------

}
