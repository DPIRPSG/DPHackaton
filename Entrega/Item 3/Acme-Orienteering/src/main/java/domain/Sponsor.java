package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class Sponsor extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String name;
	private String description;
	private String logo;
	
	@NotNull
	@NotBlank
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
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
	@URL
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	// Relationships ----------------------------------------------------------
	private Collection<Finances> finances;

	@NotNull
	@Valid
	@OneToMany(mappedBy = "sponsor")
	public Collection<Finances> getFinances() {
		return finances;
	}
	public void setFinances(Collection<Finances> finances) {
		this.finances = finances;
	}

	
}
