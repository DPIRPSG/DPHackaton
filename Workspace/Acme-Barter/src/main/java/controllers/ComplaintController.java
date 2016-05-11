package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.Barter;
import domain.Complaint;
import domain.Match;

import services.ActorService;
import services.BarterService;
import services.ComplaintService;
import services.MatchService;

@Controller
@RequestMapping("/complaint")
public class ComplaintController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private ComplaintService complaintService;

	@Autowired
	private MatchService matchService;
	
	@Autowired
	private BarterService barterService;
	
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public ComplaintController() {
		super();
	}
	
	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int barterOrMatchId) {
		ModelAndView result;
		Collection<Complaint> complaints;
		String barterOrMatchName;
		Integer userId;

		complaints = complaintService.findAllByBarterOrMatch(barterOrMatchId);
		
		barterOrMatchName = null;
		userId = null;
		
		try{
			
			Barter barter;
			
			barter = barterService.findOne(barterOrMatchId);
			
			barterOrMatchName = barter.getTitle();
			
		}catch(Exception barterException){
			
			try{
				
				Match match;
				
				match = matchService.findOne(barterOrMatchId);
				
				barterOrMatchName = match.getCreatorBarter().getTitle() + " - " + match.getReceiverBarter().getTitle();
				
			}catch(Exception matchAndBarterException){
				
				Assert.isTrue(false, "You have to choose a Barter or a Match to see the Complaints asociated.");
				
			}
			
		}
		
		if(actorService.checkAuthority("USER")){
			userId = actorService.findByPrincipal().getId();
		}
		
		Assert.notNull(barterOrMatchName, "You have to choose a Barter or a Match to see the Complaints asociated.");
		
		result = new ModelAndView("complaint/list");
		result.addObject("requestURI", "complaint/list.do");
		result.addObject("complaints", complaints);
		result.addObject("barterOrMatchId", barterOrMatchId);
		result.addObject("barterOrMatchName", barterOrMatchName);
		if(actorService.checkAuthority("USER")){
			result.addObject("userId", userId);
		}
		

		return result;
	}

}
