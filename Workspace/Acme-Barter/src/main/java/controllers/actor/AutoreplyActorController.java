package controllers.actor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AutoreplyService;
import controllers.AbstractController;
import domain.Autoreply;

@Controller
@RequestMapping(value = "/autoreply/actor")
public class AutoreplyActorController extends AbstractController {

	// Services ----------------------------------------------------------

	@Autowired
	private AutoreplyService autoreplyService;

	// Constructors ----------------------------------------------------------

	public AutoreplyActorController() {
		super();
	}

	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Autoreply> autoreplies;

		autoreplies = autoreplyService.findByPrincipal();

		result = new ModelAndView("autoreply/list");
		result.addObject("autoreplies", autoreplies);
		result.addObject("requestURI", "autoreply/actor/list.do");

		return result;
	}

	// Creation ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Autoreply autoreply;

		autoreply = autoreplyService.create();

		result = createEditModelAndView(autoreply);

		return result;
	}

	// Edition ----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = true) int autoreplyId) {
		ModelAndView result;
		Autoreply autoreply;

		autoreply = autoreplyService.findToEdit(autoreplyId);

		result = createEditModelAndView(autoreply);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Autoreply autoreply, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(autoreply);
		} else {
			try {
				autoreplyService.saveFromEdit(autoreply);
				result = new ModelAndView("redirect:/autoreply/actor/list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(autoreply,
						"autoreply.commit.error");
			}
		}
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid Autoreply autoreply, BindingResult binding) {
		ModelAndView result;

		try {
			autoreplyService.deleteFromEdit(autoreply);
			result = new ModelAndView("redirect:/autoreply/actor/list.do");
		} catch (Throwable oops) {
			result = createEditModelAndView(autoreply, "autoreply.commit.error");
		}
		return result;
	}

	// Ancillary Methods
	// ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Autoreply input) {
		ModelAndView result;

		result = createEditModelAndView(input, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Autoreply input,
			String message) {
		ModelAndView result;

		result = new ModelAndView("autoreply/edit");
		result.addObject("autoreply", input);
		result.addObject("message", message);

		return result;
	}

}
