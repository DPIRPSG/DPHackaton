package domain;

import java.util.Collection;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String description;
	private String name;
	
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

	// Relationships ----------------------------------------------------------
	private Collection<Race> race;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "category")
	public Collection<Race> getRace() {
		return race;
	}
	public void setRace(Collection<Race> race) {
		this.race = race;
	}
}
