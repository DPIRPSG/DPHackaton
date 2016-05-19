package controllers.referee;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.LeagueService;
import services.PunishmentService;
import controllers.AbstractController;
import domain.Actor;
import domain.League;
import domain.Punishment;

@Controller
@RequestMapping("/punishment/referee")
public class PunishmentRefereeController extends AbstractController {

	// Services ----------------------------------------------------------
	
	@Autowired
	private PunishmentService punishmentService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private ActorService actorService;
	
	// Constructors --------------------------------------------------------
	
	public PunishmentRefereeController() {
		super();
	}
	
	// Creating ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam Integer clubId) {
		ModelAndView result;
		Punishment punishment;
		Actor referee;
		Collection<League> leagues;

		punishment = punishmentService.create(clubId);
		referee = actorService.findByPrincipal();
		leagues = leagueService.findAllByRefereeAndClubId(referee.getId(), clubId);
		
		result = createEditModelAndView(punishment, leagues);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Punishment punishment, BindingResult binding) {
		ModelAndView result;
		Actor referee;
		Collection<League> leagues;
		
		referee = actorService.findByPrincipal();
		leagues = leagueService.findAllByRefereeAndClubId(referee.getId(), punishment.getClub().getId());

		if (binding.hasErrors()) {
			result = createEditModelAndView(punishment, leagues);
		} else {
			try {
				punishmentService.save(punishment);
				result = new ModelAndView("redirect:../../punishment/list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(punishment, leagues, "punishment.commit.error");
			}
		}

		return result;
	}
	
	// Ancillary Methods ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Punishment punishment, Collection<League> leagues) {
		ModelAndView result;

		result = createEditModelAndView(punishment, leagues, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Punishment punishment, Collection<League> leagues, String message) {
		ModelAndView result;

		result = new ModelAndView("punishment/create");
		result.addObject("punishment", punishment);
		result.addObject("leagues", leagues);
		result.addObject("message", message);

		return result;
	}
	
}
