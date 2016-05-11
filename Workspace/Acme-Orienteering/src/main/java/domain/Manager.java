package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	
	// Relationships ----------------------------------------------------------
	private Club club;

	@Valid
	@OneToOne(optional = true, mappedBy = "manager")
	public Club getClub() {
		return club;
	}
	public void setClub(Club club) {
		this.club = club;
	}
	

}
