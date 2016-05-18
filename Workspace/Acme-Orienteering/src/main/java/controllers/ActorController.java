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
import controllers.AbstractController;
import domain.Actor;
import domain.Curriculum;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {
	
	// Services ----------------------------------------------------------
	
	@Autowired
	private ActorService actorService;
	
	
	// Constructors --------------------------------------------------------
	
	public ActorController() {
		super();
	}
	
	// Listing --------------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int actorId) {
		ModelAndView result;
		Actor actor;
		Collection<String> skills, likes, dislikes;
		Curriculum curriculum;
		
		skills = new ArrayList<String>();
		likes = new ArrayList<String>();
		dislikes = new ArrayList<String>();
				
		actor = actorService.findOne(actorId);
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
		
		
		result = new ModelAndView("actor/list");
		result.addObject("requestURI", "actor/list.do");
		result.addObject("actor", actor);
		result.addObject("skills", skills);
		result.addObject("likes", likes);
		result.addObject("dislikes", dislikes);

		return result;
	}
	
}
