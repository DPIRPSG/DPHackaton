package controllers.actor;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CurriculumService;
import controllers.AbstractController;
import domain.Actor;
import domain.Curriculum;

@Controller
@RequestMapping("/curriculum/actor")
public class CurriculumActorController extends AbstractController {
	
	// Services ----------------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private CurriculumService curriculumService;
	
	// Constructors --------------------------------------------------------
	
	public CurriculumActorController() {
		super();
	}
	
	// Listing --------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer actorId) {
		ModelAndView result;
		Actor actor;
		Curriculum curriculum;
		Collection<String> skills, likes, dislikes;
		String actorFullName;
		boolean isMine;
		
		skills = new ArrayList<String>();
		likes = new ArrayList<String>();
		dislikes = new ArrayList<String>();
				
		if(actorId == null){
			actor = actorService.findByPrincipal();
			isMine = true;
		}else{
			actor = actorService.findOne(actorId);
			isMine = false;
		}
		
		curriculum = actor.getCurriculum();
		
		if (curriculum != null) {
			String[] skillsComoArray = curriculum.getSkills().split(";");
			for (int i = 0; i < skillsComoArray.length; i++) {
				skills.add(skillsComoArray[i]);
			}

			String[] likesComoArray = curriculum.getLikes().split(";");
			for (int i = 0; i < likesComoArray.length; i++) {
				likes.add(likesComoArray[i]);
			}

			String[] dislikesComoArray = curriculum.getDislikes().split(";");
			for (int i = 0; i < dislikesComoArray.length; i++) {
				dislikes.add(dislikesComoArray[i]);
			}
		}
		
		actorFullName = actor.getName() + " " + actor.getSurname();
		
		result = new ModelAndView("curriculum/list");
		result.addObject("requestURI", "curriculum/trainer/list.do");
		result.addObject("curriculum", curriculum);
		result.addObject("skills", skills);
		result.addObject("likes", likes);
		result.addObject("dislikes", dislikes);
		result.addObject("actorFullName", actorFullName);
		result.addObject("isMine", isMine);

		return result;
	}
	
	// Creating ----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Curriculum curriculum;

		curriculum = curriculumService.create();
		
		result = createEditModelAndView(curriculum);

		return result;
	}

	// Editing ----------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Actor actor;
		Curriculum curriculum;

		actor = actorService.findByPrincipal();
		curriculum = actor.getCurriculum();
		
		Assert.notNull(curriculum);
		
		result = createEditModelAndView(curriculum);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid Curriculum curriculum, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = createEditModelAndView(curriculum);
		} else {
			try {
				curriculumService.save(curriculum);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				result = createEditModelAndView(curriculum, "curriculum.commit.error");
			}
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(Curriculum curriculum, BindingResult binding) {
		ModelAndView result;

		try {
			curriculumService.delete(curriculum);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = createEditModelAndView(curriculum, "curriculum.commit.error");
		}

		return result;
	}
	
	// Ancillary Methods ----------------------------------------------------------

	protected ModelAndView createEditModelAndView(Curriculum curriculum) {
		ModelAndView result;

		result = createEditModelAndView(curriculum, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Curriculum curriculum, String message) {
		ModelAndView result;

		result = new ModelAndView("curriculum/edit");
		result.addObject("curriculum", curriculum);
		result.addObject("message", message);

		return result;
		}
}
