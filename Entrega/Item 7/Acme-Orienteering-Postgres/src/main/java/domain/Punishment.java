package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Punishment extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String reason;
	private int points;
	
	@NotNull
	@NotBlank
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Min(value = 1)
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	// Relationships ----------------------------------------------------------
	private League league;
	private Club club;

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
	@ManyToOne(optional = false)
	public Club getClub() {
		return club;
	}
	public void setClub(Club club) {
		this.club = club;
	}
}
