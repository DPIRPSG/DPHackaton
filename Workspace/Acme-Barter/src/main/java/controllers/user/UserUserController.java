package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
import services.form.ActorFormService;

import controllers.AbstractController;
import domain.User;
import domain.form.ActorForm;

@Controller
@RequestMapping(value = "/user/user")
public class UserUserController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private UserService userService;
	
	@Autowired
	private ActorFormService actorFormService;
	
	@Autowired
	private Validator actorFormValidator;

	// Constructors ----------------------------------------------------------

	public UserUserController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(){
		ModelAndView result;
		User customer;
		
		customer = userService.findByPrincipal();
		
		result = new ModelAndView("user/display");
		result.addObject("user", customer);
		
		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(){
		ModelAndView result;
		ActorForm actorForm;
		
		actorForm = actorFormService.createForm();
		
		result = createEditModelAndView(actorForm);
		
		return result;
	}

	// Edition ----------------------------------------------------------

	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid ActorForm customer, BindingResult binding) {
		actorFormValidator.validate(customer, binding);
		
		ModelAndView result;
		
		if(binding.hasErrors()) {
			result = createEditModelAndView(customer);
		} else {
			try {
				actorFormService.saveForm(customer);
				result = new ModelAndView("redirect:display.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(customer, "user.commit.error");				
			}
		}

		return result;
	}
	
	@RequestMapping(value = "/followOrUnfollow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam(required=true) int userIdOtherUser
			,@RequestParam(required=false, defaultValue="user/list.do") String redirectUri
			){
		ModelAndView result;

		result = new ModelAndView("redirect:../../" + redirectUri);
		try {
			userService.followOrUnfollowById(userIdOtherUser);
			result.addObject("messageStatus", "user.followOrUnfollow.ok");				
		} catch (Throwable oops) {
			result.addObject("messageStatus", "user.commit.error");				
		}
		
		return result;
	}
	
	/** A los que sigo
	 * 
	 * @param userIdOtherUser
	 * @return
	 */
	@RequestMapping(value = "/followed", method = RequestMethod.GET)
	public ModelAndView followed(){
		ModelAndView result;

		Collection<User> followed;
		
		followed = userService.getFollowed();
		
		result = new ModelAndView("user/list");
		result.addObject("users", followed);
		result.addObject("requestURI", "user/user/followed.do");
		result.addObject("IfollowTo", userService.getFollowed());
		
		return result;
	}
	
	/** Los que me siguen
	 * 
	 * @param userIdOtherUser
	 * @return
	 */
	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ModelAndView followers(){
		ModelAndView result;

		Collection<User> followers;
		
		followers = userService.getFollowers();
		
		result = new ModelAndView("user/list");
		result.addObject("users", followers);
		result.addObject("requestURI", "user/user/followers.do");
		result.addObject("IfollowTo", userService.getFollowed());
		
		return result;
	}
	
	
	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(ActorForm customer) {
		ModelAndView result;

		result = createEditModelAndView(customer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(ActorForm customer, String message) {
		ModelAndView result;

		result = new ModelAndView("actorForm/edit");
		result.addObject("actorForm", customer);
		result.addObject("message", message);
		result.addObject("urlAction", "user/user/edit.do");

		return result;
	}
}
