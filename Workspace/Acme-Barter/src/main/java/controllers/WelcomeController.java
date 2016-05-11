/* WelcomeController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Services ----------------------------------------------------------
	
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}

	// Index ------------------------------------------------------------------

	@RequestMapping(value = "/index")
	public ModelAndView index(
			@RequestParam(required = false, defaultValue = "") String messageStatus
			,@CookieValue(value = "createSocialIdentity", required = false, defaultValue = "false") String createSocialIdentity
			, HttpServletResponse response
			) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
//		Collection<Activity> activities;
//		Activity activity;
		int actorId;
		
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		result = new ModelAndView("welcome/index");
		result.addObject("moment", moment);
//		result.addObject("activity", activity);
		
		if(messageStatus != ""){
			result.addObject("messageStatus", messageStatus);
		}
		
		try{
			actorId = actorService.findByPrincipal().getId();
		}catch (Exception e) {
			actorId = 0;
		}
		
		if(createSocialIdentity.equals(String.valueOf(actorId) + "true") && actorService.checkAuthority("USER")){
			result = new ModelAndView("redirect:/socialIdentity/user/edit.do");			
		}

		return result;
	}
}