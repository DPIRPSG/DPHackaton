package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import domain.SocialIdentity;
import domain.User;
import services.ActorService;
import services.SocialIdentityService;
import services.UserService;

@Controller
@RequestMapping(value = "/socialIdentity")
public class SocialIdentityController extends AbstractController{

	//Services ----------------------------------------------------------

	@Autowired
	private SocialIdentityService socialIdentityService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private UserService userService;
	
	//Constructors ----------------------------------------------------------
	
	public SocialIdentityController(){
		super();
	}

	//Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required=true) String userId){
		ModelAndView result;
		Collection<SocialIdentity> socialIdentities;
		
		socialIdentities = socialIdentityService.findByUserId(Integer.valueOf(
				userId).intValue());
				
		result = new ModelAndView("socialIdentity/list");
		result.addObject("socialIdentities", socialIdentities);
		result.addObject("requestURI", "socialIdentity/list.do");
		if(actorService.checkAuthority("USER")){
			User us;
			
			us = userService.findByPrincipal();
			result.addObject("actUserId", us.getId());
		}
		
		return result;
	}
	//Creation ----------------------------------------------------------
	
	//Edition ----------------------------------------------------------

	//Ancillary Methods ----------------------------------------------------------

}
