package domain.form;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class CommentForm{

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String text;
	private int starRating;
	private int commentId;
	private int commentedEntityId;
	
	@NotBlank
	@NotNull
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	//No debe ser null
	@Range(min = 0, max = 5)
	@Valid
	public int getStarRating() {
		return starRating;
	}
	public void setStarRating(int starRating) {
		this.starRating = starRating;
	}
	
	@NotNull
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int comment) {
		this.commentId = comment;
	}
	
	@NotNull
	public int getCommentedEntityId() {
		return commentedEntityId;
	}
	public void setCommentedEntityId(int commentedEntity) {
		this.commentedEntityId = commentedEntity;
	}
	
	
}
