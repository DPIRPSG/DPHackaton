package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Club extends CommentedEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String description;
	private String name;
	private Collection<String> pictures;
	private Date crationMoment;
	private boolean deleted;
	
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
	
	@NotNull
	@ElementCollection
	@URL
	public Collection<String> getPictures() {
		return pictures;
	}
	public void setPictures(Collection<String> pictures) {
		this.pictures = pictures;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getCrationMoment() {
		return crationMoment;
	}
	public void setCrationMoment(Date crationMoment) {
		this.crationMoment = crationMoment;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	// Relationships ----------------------------------------------------------
	private Manager manager;
	private Collection<Bulletin> bulletins;
	private Collection<Classification> classifications;
	private Collection<Entered> entered;
	private Collection<Punishment> punishments;
	private Collection<FeePayment> feePayments;

	@Valid
	@NotNull
	@OneToOne(optional = false, mappedBy = "club")
	public Manager getManager() {
		return manager;
	}
	public void setManager(Manager manager) {
		this.manager = manager;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "club")
	public Collection<Bulletin> getBulletins() {
		return bulletins;
	}
	public void setBulletins(Collection<Bulletin> bulletins) {
		this.bulletins = bulletins;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "club")
	public Collection<Classification> getClassifications() {
		return classifications;
	}
	public void setClassifications(Collection<Classification> classifications) {
		this.classifications = classifications;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "club")	
	public Collection<Entered> getEntered() {
		return entered;
	}
	public void setEntered(Collection<Entered> entered) {
		this.entered = entered;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "club")
	public Collection<Punishment> getPunishments() {
		return punishments;
	}
	public void setPunishments(Collection<Punishment> punishments) {
		this.punishments = punishments;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "club")
	public Collection<FeePayment> getFeePayments() {
		return feePayments;
	}
	public void setFeePayments(Collection<FeePayment> feePayments) {
		this.feePayments = feePayments;
	}
}
