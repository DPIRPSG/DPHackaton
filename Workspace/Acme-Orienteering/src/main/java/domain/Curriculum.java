package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Curriculum extends CommentedEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String statement;
	private String skills;
	private String likes;
	private String dislikes;
	
	@NotNull
	@NotBlank
	public String getStatement() {
		return statement;
	}
	public void setStatement(String statement) {
		this.statement = statement;
	}

	@NotNull
	@NotBlank
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	
	@NotNull
	@NotBlank
	public String getLikes() {
		return likes;
	}
	public void setLikes(String likes) {
		this.likes = likes;
	}
	
	@NotNull
	@NotBlank
	public String getDislikes() {
		return dislikes;
	}
	public void setDislikes(String dislikes) {
		this.dislikes = dislikes;
	}
	
	// Relationships ----------------------------------------------------------
	private Actor actor;

	@Valid
	@NotNull
	@OneToOne(optional=false, mappedBy = "curriculum")
	public Actor getActor() {
		return actor;
	}
	public void setActor(Actor actor) {
		this.actor = actor;
	}
}
