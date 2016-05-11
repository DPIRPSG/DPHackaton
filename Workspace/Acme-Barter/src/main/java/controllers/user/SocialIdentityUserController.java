package controllers.user;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SocialIdentityService;
import services.UserService;

import controllers.AbstractController;
import domain.SocialIdentity;

@Controller
@RequestMapping(value = "/socialIdentity/user")
public class SocialIdentityUserController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private SocialIdentityService socialIdentityService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserService userService;

	// Constructors ----------------------------------------------------------

	public SocialIdentityUserController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView display(){
		ModelAndView result;
		Collection<SocialIdentity> socialIdent;
		
		socialIdent = socialIdentityService.findByPrincipal();
		
		result = new ModelAndView("socialIdentity/list");
		result.addObject("socialIdentities", socialIdent);
		result.addObject("actUserId", userService.findByPrincipal().getId());
		result.addObject("requestURI", "socialIdentity/user/list.do");
		result.addObject("isProperty", true);
		
		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create(
			@RequestParam(required = false, defaultValue = "") String socialIdentityId) {
		ModelAndView result;
		
		SocialIdentity socialIdentity;

		// Si no la tiene debería crearla
		socialIdentity = socialIdentityService.findOrCreateById(socialIdentityId);

		result = createEditModelAndView(socialIdentity);

		return result;
	}

	// Edition ----------------------------------------------------------

	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid SocialIdentity socialIdentity, BindingResult binding
			, HttpServletResponse response			
			) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(socialIdentity);
		} else {
			try {
				Cookie cook1;
				int actId;
				
				actId = actorService.findByPrincipal().getId();
				
				cook1 = new Cookie("createSocialIdentity", String.valueOf(actId) + "false");
				cook1.setPath("/");
			
				response.addCookie(cook1);
				
				socialIdentityService.saveFromEdit(socialIdentity);

				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(socialIdentity, "socialIdentity.commit.error");				
			}
		}

		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SocialIdentity socialIdentity, BindingResult binding) {
		ModelAndView result;

		try {
			socialIdentityService.deleteFromEdit(socialIdentity);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = createEditModelAndView(socialIdentity, "socialIdentity.commit.error");
		}

		return result;
	}
	

	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(SocialIdentity socialIdentity) {
		ModelAndView result;

		result = createEditModelAndView(socialIdentity, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(SocialIdentity socialIdentity, String message) {
		ModelAndView result;

		result = new ModelAndView("socialIdentity/edit");
		result.addObject("socialIdentity", socialIdentity);
		result.addObject("message", message);

		return result;
	}
}
