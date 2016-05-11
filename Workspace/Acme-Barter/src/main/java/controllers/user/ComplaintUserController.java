package controllers.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ComplaintService;
import controllers.AbstractController;
import domain.Complaint;

@Controller
@RequestMapping("/complaint/user")
public class ComplaintUserController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private ComplaintService complaintService;

	// Constructors -----------------------------------------------------------

	public ComplaintUserController() {
		super();
	}
	
	// Creation ---------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam int barterOrMatchId) {
		ModelAndView result;
		Complaint complaint;

		complaint = complaintService.create(barterOrMatchId);
		
		result = createEditModelAndView(complaint, barterOrMatchId);

		return result;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Complaint complaint, BindingResult binding) {
		ModelAndView result;
		int barterOrMatchId;
		
		if(complaint.getMatch() != null){
			barterOrMatchId = complaint.getMatch().getId();
		}else{
			barterOrMatchId = complaint.getBarter().getId();
		}

		if (binding.hasErrors()) {
			result = createEditModelAndView(complaint, barterOrMatchId);
		} else {
			try {
				complaintService.save(complaint);
				result = new ModelAndView("redirect:../list.do?barterOrMatchId=" + barterOrMatchId);
			} catch (Throwable oops) {
				result = createEditModelAndView(complaint, barterOrMatchId, "complaint.commit.error");
			}
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
	
	protected ModelAndView createEditModelAndView(Complaint complaint, int barterOrMatchId) {
		ModelAndView result;

		result = createEditModelAndView(complaint, barterOrMatchId, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(Complaint complaint, int barterOrMatchId, String message) {
		ModelAndView result;
		
		result = new ModelAndView("complaint/create");
		result.addObject("complaint", complaint);
		result.addObject("barterOrMatchId", barterOrMatchId);
		result.addObject("message", message);

		return result;
	}

}
