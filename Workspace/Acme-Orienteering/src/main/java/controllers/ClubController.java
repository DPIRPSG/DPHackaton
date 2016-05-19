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

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ClubService;
import services.ManagerService;
import controllers.AbstractController;
import domain.Club;
import domain.Manager;

@Controller
@RequestMapping("/club")
public class ClubController extends AbstractController {
	
	// Services ---------------------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private ManagerService managerService;
	
	// Constructors -----------------------------------------------------------
	
	public ClubController() {
		super();
	}

	// Listing ----------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam (required = false) Integer leagueId, 
			@RequestParam (required = false, defaultValue = "false") String classification) {
		ModelAndView result;
		Collection<Club> clubes;
		Manager manager;
		Collection<ArrayList<Integer>> ranking;
		
		
		if(actorService.checkAuthority("MANAGER")) {
			manager = managerService.findByPrincipal();
		} else {
			manager = null;
		}

		if(leagueId != null) {
			if(classification.equals("true")) {
				clubes = clubService.findAllByLeagueId(leagueId);
				ranking = clubService.calculateRankingByLeague(leagueId);
			} else {
				clubes = clubService.findAllByLeagueId(leagueId);
				ranking = null;
			}
		} else {
			clubes = clubService.findAll();
			ranking = null;
		}
		
		result = new ModelAndView("club/list");
		result.addObject("requestURI", "club/list.do");
		result.addObject("clubes", clubes);
		result.addObject("manager", manager);
		result.addObject("ranking", ranking);

		return result;
	}

	// Creation ---------------------------------------------------------------

}