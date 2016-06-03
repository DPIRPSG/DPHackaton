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
	private Collection<Entered> entered;
	private Collection<Participates> participates;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "runner")
	public Collection<Entered> getEntered() {
		return entered;
	}
	public void setEntered(Collection<Entered> entered) {
		this.entered = entered;
	}

	@Valid
	@NotNull
	@OneToMany(mappedBy = "runner")
	public Collection<Participates> getParticipates() {
		return participates;
	}
	public void setParticipates(Collection<Participates> participates) {
		this.participates = participates;
	}
}
