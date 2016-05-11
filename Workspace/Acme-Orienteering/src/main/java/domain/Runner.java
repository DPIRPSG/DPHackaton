package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Runner extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<Club> entered;
	private Collection<Race> participates;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "runner")
	public Collection<Club> getEntered() {
		return entered;
	}
	public void setEntered(Collection<Club> entered) {
		this.entered = entered;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "runner")
	public Collection<Race> getParticipates() {
		return participates;
	}
	public void setParticipates(Collection<Race> participates) {
		this.participates = participates;
	}
}
