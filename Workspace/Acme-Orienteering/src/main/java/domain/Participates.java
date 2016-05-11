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
public class Participates extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private int result;

	@Min(value = 1)
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	
	// Relationships ----------------------------------------------------------
	private Race race;
	private Runner runner;

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
	public Runner getRunner() {
		return runner;
	}
	public void setRunner(Runner runner) {
		this.runner = runner;
	}
	
	
}
