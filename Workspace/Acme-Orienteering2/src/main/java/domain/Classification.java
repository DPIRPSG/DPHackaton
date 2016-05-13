package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Classification extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private int position;
	private int points;
	
	@Min(value = 1)
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Min(value = 0)
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	// Relationships ----------------------------------------------------------
	private Race race;
	private Club club;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Race getRace() {
		return race;
	}
	public void setRace(Race race) {
		this.race = race;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Club getClub() {
		return club;
	}
	public void setClub(Club club) {
		this.club = club;
	}
}
