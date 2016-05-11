package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "name"), @Index(columnList = "description") })
public class Item extends DomainEntity{

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String name;
	private String description;
	private Collection<String> pictures;
	
	@NotBlank
	@NotNull
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	@NotNull
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@NotNull
	@ElementCollection
	public Collection<String> getPictures() {
		return pictures;
	}
	public void setPictures(Collection<String> pictures) {
		this.pictures = pictures;
	}


	// Relationships ----------------------------------------------------------

	private Collection<AttributeDescription> attributesDescription;

	@Valid
	@NotNull
	@OneToMany(mappedBy="item")
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
