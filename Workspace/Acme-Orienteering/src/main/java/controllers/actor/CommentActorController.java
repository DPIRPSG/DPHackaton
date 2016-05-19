package controllers.actor;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommentService;
import services.CommentedEntityService;
import services.form.CommentFormService;
import controllers.AbstractController;
import domain.Comment;
import domain.CommentedEntity;
import domain.form.CommentForm;

@Controller
@RequestMapping("/comment/actor")
public class CommentActorController extends AbstractController {
	
	// Services ----------------------------------------------------------
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentedEntityService commentedEntityService;
	
	@Autowired
	private CommentFormService commentFormService;
	
	// Constructors --------------------------------------------------------
	
	public CommentActorController() {
		super();
	}
	
	
	// Creating --------------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam Integer commentedEntityId) {
		ModelAndView result;
		CommentForm commentForm;
		CommentedEntity commentedEntity;
		
		commentForm = commentFormService.create(commentedEntityId);
		commentedEntity = commentedEntityService.findOne(commentedEntityId);
		
		result = createEditModelAndView(commentForm, commentedEntity);
		
		return result;
	}
	
	@RequestMapping(value="/create", method=RequestMethod.POST, params="save")
	public ModelAndView save(@Valid CommentForm commentForm, BindingResult binding) {
		ModelAndView result;
		CommentedEntity commentedEntity;
		int commentedEntityId;
		
//		//commentedEntity = comment.getCommentedEntity();
//		commentedEntity = commentedEntityService.findOne(commentedEntityId);
//		
//		comment.setCommentedEntity(commentedEntity);
//		
//		//commentedEntityId = commentedEntity.getId();
//		
//		if (binding.hasErrors()) {
//			result = createEditModelAndView(comment, commentedEntity);
//		} else {
//			try {
//				commentService.save(comment);
//				result = new ModelAndView("redirect:../list.do?commentedEntityId=" + commentedEntityId);
//			} catch (Throwable oops) {
//				result = createEditModelAndView(comment, commentedEntity, "comment.commit.error");
//			}
//		}
//		
//		return result;
		
		Comment comment;
		commentedEntity = commentedEntityService.findOne(commentForm.getCommentedEntityId());
		commentedEntityId = commentedEntity.getId();
		
		if (binding.hasErrors()) {
			result = createEditModelAndView(commentForm, commentedEntity);
		} else {
			try {
				comment = commentFormService.reconstruct(commentForm);
				commentService.save(comment);
				result = new ModelAndView("redirect:../list.do?commentedEntityId=" + commentedEntityId);
			} catch (Throwable oops) {
				result = createEditModelAndView(commentForm, commentedEntity, "comment.commit.error");
			}
		}
		
		return result;
	}
	
	
	// Ancillary methods ---------------------------------------------------
	
	protected ModelAndView createEditModelAndView(CommentForm commentForm, CommentedEntity commentedEntity) {
		ModelAndView result;
		
		result = createEditModelAndView(commentForm, commentedEntity, null);
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(CommentForm commentForm, CommentedEntity commentedEntity, String message) {
		ModelAndView result;
		
		result = new ModelAndView("comment/create");
		result.addObject("commentForm", commentForm);
		result.addObject("commentedEntity", commentedEntity);
		result.addObject("message", message);
		
		return result;
	}
}
