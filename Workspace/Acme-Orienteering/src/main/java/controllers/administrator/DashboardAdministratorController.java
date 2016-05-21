package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ClubService;
import services.LeagueService;
import services.RaceService;

import controllers.AbstractController;
import domain.Category;
import domain.Club;
import domain.League;

@Controller
@RequestMapping("/dashboard/administrator")
public class DashboardAdministratorController extends AbstractController {
	
	// Services ----------------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private RaceService raceService;
	
	@Autowired
	private CategoryService categoryService;
	
	// Constructors --------------------------------------------------------
	
	public DashboardAdministratorController() {
		super();
	}
	
	
	// Listing ------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		// Declaración de variables
		Collection<Club> findAllWhoHaveWonMoreLeagues;
		Collection<Club> findAllWhoHaveWonMoreRaces;
		Collection<Club> findAllWhoHaveMoreDeniedEntered;
		Collection<Club> findAllWhoHaveMorePunishments;
		Double ratioOfClubsByLeague;
		Collection<League> findAllWhoHaveMoreRaces;
		Double ratioOfRacesByLeague;
		Collection<League> findAllWhoHaveMoreClubs;
		Collection<Category> findAllMostFrequentInRaces;
		Collection<Club> findAllWithMorePoints;
		Collection<Club> findAllWithLessPoint;
		
		// Llamamos a los servicios	
		
		findAllWhoHaveWonMoreLeagues = clubService.findAllWhoHaveWonMoreLeagues();
		findAllWhoHaveWonMoreRaces = clubService.findAllWhoHaveWonMoreRaces();
		findAllWhoHaveMoreDeniedEntered = clubService.findAllWhoHaveMoreDeniedEntered();
		findAllWhoHaveMorePunishments = clubService.findAllWhoHaveMorePunishments();
		ratioOfClubsByLeague = clubService.ratioOfClubsByLeague();
		findAllWhoHaveMoreRaces = leagueService.findAllWhoHaveMoreRaces();
		ratioOfRacesByLeague = raceService.ratioOfRacesByLeague();
		findAllWhoHaveMoreClubs = leagueService.findAllWhoHaveMoreClubs();
		findAllMostFrequentInRaces = categoryService.findAllMostFrequentInRaces();
		findAllWithMorePoints = clubService.findAllWithMorePoints();
		findAllWithLessPoint = clubService.findAllWithLessPoint();
				
		result = new ModelAndView("administrator/list");
				
		// Lo añadimos a la vista
		result.addObject("findAllWhoHaveWonMoreLeagues", findAllWhoHaveWonMoreLeagues);
		result.addObject("findAllWhoHaveWonMoreRaces", findAllWhoHaveWonMoreRaces);
		result.addObject("findAllWhoHaveMoreDeniedEntered", findAllWhoHaveMoreDeniedEntered);
		result.addObject("findAllWhoHaveMorePunishments", findAllWhoHaveMorePunishments);
		result.addObject("ratioOfClubsByLeague", ratioOfClubsByLeague);
		result.addObject("findAllWhoHaveMoreRaces", findAllWhoHaveMoreRaces);
		result.addObject("ratioOfRacesByLeague", ratioOfRacesByLeague);
		result.addObject("findAllWhoHaveMoreClubs", findAllWhoHaveMoreClubs);
		result.addObject("findAllMostFrequentInRaces", findAllMostFrequentInRaces);
		result.addObject("findAllWithMorePoints", findAllWithMorePoints);
		result.addObject("findAllWithLessPoint", findAllWithLessPoint);

		return result;
	}	
}
