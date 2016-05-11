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
public class Attribute extends DomainEntity{

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String name;
	
	@NotNull
	@NotBlank
	public String getName() {
		return name;
	}
	public void setName(String value) {
		this.name = value;
	}
	
	
	// Relationships ----------------------------------------------------------
	private Collection<AttributeDescription> attributesDescription;

	@Valid
	@NotNull
	@OneToMany(mappedBy="attribute")
	public Collection<AttributeDescription> getAttributesDescription() {
		return attributesDescription;
	}
	public void setAttributesDescription(Collection<AttributeDescription> attributesDescription) {
		this.attributesDescription = attributesDescription;
	}
	
	public void addAttributeDescription(AttributeDescription attributeDescription) {
		this.attributesDescription.add(attributeDescription);
	}

	public void removeAttributeDescription(AttributeDescription attributeDescription) {
		this.attributesDescription.remove(attributeDescription);
	}
		
}
