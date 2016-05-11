package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditorService;
import services.MatchService;
import services.UserService;
import domain.Auditor;
import domain.Match;
import domain.User;

@Controller
@RequestMapping("/match")
public class MatchController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private AuditorService auditorService;

	@Autowired
	private UserService userService;
	
	// Constructors -----------------------------------------------------------

	public MatchController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listByUser(@RequestParam(required=true) int userId) {
		ModelAndView result;
		Collection<Match> matches;
		User user;
		int userPrincipalId;
		
		try{
			user = userService.findByPrincipal();
			userPrincipalId = user.getId();
		}catch(Exception e){
			userPrincipalId = 0;
		}
		
		
		if(actorService.checkAuthority("AUDITOR") || actorService.checkAuthority("ADMIN"))
			matches = matchService.findAllUserInvolvesIncludeCancelled(userId);
		else
			matches = matchService.findAllUserInvolves(userId);
		
		result = new ModelAndView("match/list");
		result.addObject("requestURI", "match/list.do?userId=" + String.valueOf(userId));
		result.addObject("matches", matches);
		result.addObject("userId", userPrincipalId);
		result.addObject("userIdListing", userId);
		result.addObject("noDisplayHeader", true);
		if(actorService.checkAuthority("AUDITOR")){
			Auditor actAuditor = auditorService.findByPrincipal();
			result.addObject("auditor_id", String.valueOf(actAuditor.getId()));
		}
		return result;
	}
}
