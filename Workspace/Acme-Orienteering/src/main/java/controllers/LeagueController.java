/* AnnouncementAdministratorController.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.LeagueService;
import controllers.AbstractController;
import domain.Actor;
import domain.League;

@Controller
@RequestMapping("/league")
public class LeagueController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------------------------
	
	public LeagueController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam (required=false) Integer clubId) {
		ModelAndView result;
		Collection<League> leagues;
		
		if(clubId != null) {
			leagues = leagueService.findAllByClubId(clubId);
		} else {
			leagues = leagueService.findAll();
		}
		
		result = new ModelAndView("league/list");
		result.addObject("requestURI", "league/list.do");
		result.addObject("leagues", leagues);
		if(actorService.checkAuthority("MANAGER")){
			Collection<League> clubLeagues;
//			Actor referee;
			
//			referee = actorService.findByPrincipal();
			
			clubLeagues = leagueService.findAllByClubId(clubId);
			
			result.addObject("clubLeagues", clubLeagues);
		}

		return result;
	}

}