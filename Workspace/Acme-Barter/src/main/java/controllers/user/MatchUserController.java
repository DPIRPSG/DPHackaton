package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BarterService;
import services.LegalTextService;
import services.MatchService;
import services.UserService;
import controllers.AbstractController;
import domain.Barter;
import domain.LegalText;
import domain.Match;
import domain.User;

@Controller
@RequestMapping("/match/user")
public class MatchUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BarterService barterService;
	
	@Autowired
	private LegalTextService legalTextService;

	// Constructors -----------------------------------------------------------

	public MatchUserController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(String requestURI) {
		ModelAndView result;
		Collection<Match> matches;
		User user;
		int userId;
		
		user = userService.findByPrincipal();
		userId = user.getId();

		matches = matchService.findAllUserInvolves(userId);
		
		result = new ModelAndView("match/list");
		result.addObject("requestURI", requestURI);
		result.addObject("matches", matches);
		result.addObject("userId", userId);

		return result;
	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Collection<Match> matches;

		matches = matchService.findAllByFollowedUser();
		
		result = new ModelAndView("match/display");
		result.addObject("requestURI", "match/user/display.do");
		result.addObject("matches", matches);

		return result;
	}
	
	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Match match;
		Collection<Barter> creatorBarters;
		Collection<Barter> receiverBarters;
		Collection<LegalText> legalTexts;
		User user;
		int userId;
		
		user = userService.findByPrincipal();
		userId = user.getId();
		
		creatorBarters = barterService.findByUserIdNotCancelledNotInMatchNotCancelled(userId);
		receiverBarters = barterService.findAllOfOtherUsersByUserIdNotCancelledNotInMatchNotCancelled(userId);
		legalTexts = legalTextService.findAll();

		match = matchService.create();
		
		result = createEditModelAndView(match, creatorBarters, receiverBarters, legalTexts);

		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Match match, BindingResult binding) {
		ModelAndView result;
		Collection<Barter> creatorBarters;
		Collection<Barter> receiverBarters;
		Collection<LegalText> legalTexts;
		User user;
		int userId;
		
		user = userService.findByPrincipal();
		userId = user.getId();
		
		creatorBarters = barterService.findByUserIdNotCancelledNotInMatchNotCancelled(userId);
		receiverBarters = barterService.findAllOfOtherUsersByUserIdNotCancelledNotInMatchNotCancelled(userId);
		legalTexts = legalTextService.findAll();

		if (binding.hasErrors()) {
			result = createEditModelAndView(match, creatorBarters, receiverBarters, legalTexts);
		} else {
			try {
				matchService.save(match);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(match, creatorBarters, receiverBarters, legalTexts, "match.commit.error");
			}
		}

		return result;
	}
	
	// Cancellation ---------------------------------------------------------------
	
	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancel(@RequestParam Integer matchId) {
		ModelAndView result;
		Match match;
		//String requestURI;

		match = matchService.findOne(matchId);
		
		matchService.cancel(match);
		
		//requestURI = "match/user/list.do";
		
		//result = this.list(requestURI);
		result = new ModelAndView("redirect:list.do");

		return result;
	}
	
	// Signing ---------------------------------------------------------------
	
	@RequestMapping(value = "/sign", method = RequestMethod.GET)
	public ModelAndView sign(@RequestParam Integer matchId) {
		ModelAndView result;
		Match match;
		//String requestURI;

		match = matchService.findOne(matchId);
		
		matchService.sign(match);
		
		//requestURI = "match/user/list.do";
		
		//result = this.list(requestURI);
		result = new ModelAndView("redirect:list.do");

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Match match, Collection<Barter> creatorBarters, Collection<Barter> receiverBarters, Collection<LegalText> legalTexts) {
		ModelAndView result;

		result = createEditModelAndView(match, creatorBarters, receiverBarters, legalTexts, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Match match, Collection<Barter> creatorBarters, Collection<Barter> receiverBarters, Collection<LegalText> legalTexts, String message) {
		ModelAndView result;
		
		result = new ModelAndView("match/create");
		result.addObject("match", match);
		result.addObject("creatorBarters", creatorBarters);
		result.addObject("receiverBarters", receiverBarters);
		result.addObject("legalTexts", legalTexts);
		result.addObject("message", message);

		return result;
	}
}
