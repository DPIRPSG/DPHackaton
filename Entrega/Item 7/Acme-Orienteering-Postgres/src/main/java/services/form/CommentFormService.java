package services.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Comment;
import domain.form.CommentForm;

import services.CommentService;

@Service
@Transactional
public class CommentFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private CommentService commentService;
	
	// Constructors -----------------------------------------------------------

	public CommentFormService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public CommentForm create(int commentedEntityId) {
		CommentForm result;
				
		result = new CommentForm();
		
		result.setCommentId(0);
		result.setCommentedEntityId(commentedEntityId);
		
		return result;
	}
	
	public Comment reconstruct(CommentForm commentForm) {
		Comment result;

		result = commentService.create(commentForm.getCommentedEntityId());
		result.setText(commentForm.getText());
		result.setStarRating(commentForm.getStarRating());

		return result;
	}
	
}
