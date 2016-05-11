package controllers.auditor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import services.MatchService;
import controllers.AbstractController;
import domain.Auditor;
import domain.Match;

@Controller
@RequestMapping(value = "/match/auditor")
public class MatchAuditorController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private AuditorService auditorService;

	// Constructors ----------------------------------------------------------

	public MatchAuditorController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/self-assign", method = RequestMethod.GET)
	public ModelAndView selfAssign(@RequestParam(required=true) int matchId,
			@RequestParam(required=true) String redirectUri){
		ModelAndView result;
		
		result = new ModelAndView("redirect:../../" + redirectUri);

		try {
			matchService.selfAssignByAuditor(matchId);
			result.addObject("messageStatus", "match.commit.ok");			
		} catch (Throwable oops) {
			result.addObject("messageStatus", "match.commit.error");			
		}

		return result;
	}
	
	@RequestMapping(value = "/list-assigned", method = RequestMethod.GET)
	public ModelAndView list(){
		ModelAndView result;
		Collection<Match> matches;
		
		matches = matchService.findAllByAuditor();
		
		result = new ModelAndView("match/list");
		result.addObject("matches", matches);
		result.addObject("requestURI", "match/auditor/list.do");
		Auditor actAuditor = auditorService.findByPrincipal();
		result.addObject("auditor_id", String.valueOf(actAuditor.getId()));
		
		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/write-report", method = RequestMethod.GET)
	public ModelAndView writeReport(@RequestParam(required=true) int matchId){
		ModelAndView result;
		Match match;
		
		match = matchService.findOne(matchId);

		result = createEditModelAndView(match);
		
		return result;
	}

	// Edition ----------------------------------------------------------

	
	@RequestMapping(value = "/write-report", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Match match, BindingResult binding) {
		ModelAndView result;
		
		if(binding.hasErrors()) {
			result = createEditModelAndView(match);
		} else {
			try {
				matchService.addReport(match);
				result = new ModelAndView("redirect:list-assigned.do");
			} catch (Throwable oops) {
				System.out.println(oops);
				result = createEditModelAndView(match, "match.commit.error");				
			}
		}

		return result;
	}
	
	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Match customer) {
		ModelAndView result;

		result = createEditModelAndView(customer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Match customer, String message) {
		ModelAndView result;

		result = new ModelAndView("match/write-report");
		result.addObject("match", customer);
		result.addObject("message", message);
		result.addObject("urlAction", "match/auditor/write-report.do");

		return result;
	}
}
