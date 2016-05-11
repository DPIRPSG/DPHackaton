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
public class Referee extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<League> league;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "referee")
	public Collection<League> getLeague() {
		return league;
	}
	public void setLeague(Collection<League> league) {
		this.league = league;
	}
}
