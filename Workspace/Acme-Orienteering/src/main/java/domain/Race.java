package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Race extends CommentedEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String description;
	private String name;
	private Date moment;

	@NotNull
	@NotBlank
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@NotBlank
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}

	// Relationships ----------------------------------------------------------
	private Collection<Classification> classifications;
	private Category category;
	private League league;
	private Collection<Participates> participates;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "race")
	public Collection<Classification> getClassifications() {
		return classifications;
	}
	public void setClassifications(Collection<Classification> classifications) {
		this.classifications = classifications;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public League getLeague() {
		return league;
	}
	public void setLeague(League league) {
		this.league = league;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "race")
	public Collection<Participates> getParticipates() {
		return participates;
	}
	public void setParticipates(Collection<Participates> participates) {
		this.participates = participates;
	}

}
