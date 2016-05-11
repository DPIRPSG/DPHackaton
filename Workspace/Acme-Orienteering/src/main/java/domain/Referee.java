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
	private Collection<League> leagues;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "referee")
	public Collection<League> getLeagues() {
		return leagues;
	}
	public void setLeagues(Collection<League> leagues) {
		this.leagues = leagues;
	}
}
