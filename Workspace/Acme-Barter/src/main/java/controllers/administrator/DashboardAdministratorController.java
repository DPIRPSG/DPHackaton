package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AuditorService;
import services.BarterService;
import services.ComplaintService;
import services.MatchService;
import services.UserService;
import controllers.AbstractController;
import domain.Auditor;
import domain.User;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {
	
	// Services ----------------------------------------------------------

	@Autowired
	private UserService userService;
	
	@Autowired
	private BarterService barterService;
	
	@Autowired
	private AuditorService auditorService;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private MatchService matchService;
	
	// Constructors --------------------------------------------------------
	
	public DashboardAdministratorController() {
		super();
	}
	
	
	// Listing ------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		
		// Level C
		Integer getTotalNumberOfUsersRegistered;
		Integer getTotalNumberOfBarterRegistered;
		Integer getTotalNumberOfBarterCancelled;
		Collection<User> getUsersAbovePencentile90;
		Collection<User> getUsersWithNoBarterThisMonth;
		
		Integer getTotalNumberOfComplaintsCreated;
		Double getAverageOfComplaintsPerBarter;
		Double getAverageOfComplaintsPerMatch;
		Collection<User> getUsersWhoHaveCreatedMoreComplaintsThatAverage;
		
		// Level B
		
		Integer minumumNumberBarterPerUser;
		Integer maximumNumberBarterPerUser;
		Double averageNumberBarterPerUser;
		Double ratioBarterNotRelatedToAnyOtherBarter;
		Collection<User> getUsersWithMoreBarters;
		Collection<User> getUsersWithMoreBartersCancelled;
		Collection<User> getUsersWithMoreMatches;
		
		// Level A
		
		Collection<Auditor> getAuditorsWithMoreMatches;
		Collection<User> getUsersWithMoreMatchesAudited;
		
		// Level C
		getTotalNumberOfUsersRegistered = userService.getTotalNumberOfUsersRegistered();
		getTotalNumberOfBarterRegistered =  barterService.getTotalNumberOfBarterRegistered();
		getTotalNumberOfBarterCancelled = barterService.getTotalNumberOfBarterCancelled();
		getUsersAbovePencentile90 = userService.getUsersAbovePencentile90();
		getUsersWithNoBarterThisMonth = userService.getUsersWithNoBarterThisMonth();
		
		getTotalNumberOfComplaintsCreated = complaintService.getTotalNumberOfComplaintsCreated();
		getAverageOfComplaintsPerBarter = barterService.getAverageOfComplaintsPerBarter();
		getAverageOfComplaintsPerMatch = matchService.getAverageOfComplaintsPerMatch();
		getUsersWhoHaveCreatedMoreComplaintsThatAverage = userService.getUsersWhoHaveCreatedMoreComplaintsThatAverage();
				
		// Level B
		minumumNumberBarterPerUser = userService.minumumNumberBarterPerUser();
		maximumNumberBarterPerUser = userService.maximumNumberBarterPerUser();
		averageNumberBarterPerUser = userService.averageNumberBarterPerUser();
		ratioBarterNotRelatedToAnyOtherBarter = barterService.ratioBarterNotRelatedToAnyOtherBarter();
		getUsersWithMoreBarters = userService.getUsersWithMoreBarters();
		getUsersWithMoreBartersCancelled = userService.getUsersWithMoreBartersCancelled();
		getUsersWithMoreMatches = userService.getUsersWithMoreMatches();
		
		// Level A
		getAuditorsWithMoreMatches = auditorService.getAuditorsWithMoreMatches();
		getUsersWithMoreMatchesAudited = userService.getUsersWithMoreMatchesAudited();		
				
		result = new ModelAndView("administrator/list");
		
		// Level C
		result.addObject("getTotalNumberOfUsersRegistered", getTotalNumberOfUsersRegistered);
		result.addObject("getTotalNumberOfBarterRegistered", getTotalNumberOfBarterRegistered);
		result.addObject("getTotalNumberOfBarterCancelled", getTotalNumberOfBarterCancelled);
		result.addObject("getUsersAbovePencentile90", getUsersAbovePencentile90);
		result.addObject("getUsersWithNoBarterThisMonth", getUsersWithNoBarterThisMonth);
		
		result.addObject("getTotalNumberOfComplaintsCreated", getTotalNumberOfComplaintsCreated);
		result.addObject("getAverageOfComplaintsPerBarter", getAverageOfComplaintsPerBarter);
		result.addObject("getAverageOfComplaintsPerMatch", getAverageOfComplaintsPerMatch);
		result.addObject("getUsersWhoHaveCreatedMoreComplaintsThatAverage", getUsersWhoHaveCreatedMoreComplaintsThatAverage);

		// Level B
		result.addObject("minumumNumberBarterPerUser", minumumNumberBarterPerUser);
		result.addObject("maximumNumberBarterPerUser", maximumNumberBarterPerUser);
		result.addObject("averageNumberBarterPerUser", averageNumberBarterPerUser);
		result.addObject("ratioBarterNotRelatedToAnyOtherBarter", ratioBarterNotRelatedToAnyOtherBarter);
		result.addObject("getUsersWithMoreBarters", getUsersWithMoreBarters);
		result.addObject("getUsersWithMoreBartersCancelled", getUsersWithMoreBartersCancelled);
		result.addObject("getUsersWithMoreMatches", getUsersWithMoreMatches);
		
		// Level A
		result.addObject("getAuditorsWithMoreMatches", getAuditorsWithMoreMatches);
		result.addObject("getUsersWithMoreMatchesAudited", getUsersWithMoreMatchesAudited);
		
		result.addObject("requestURI", "dashboard/administrator/list.do");
		
		return result;
	}	
}
