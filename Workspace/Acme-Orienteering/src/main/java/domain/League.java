package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class League extends CommentedEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String description;
	private String name;
	private Collection<String> pictures;
	private Date crationMoment;
	private Date startedMoment;
	private double amount;
	
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
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getStartedMoment() {
		return startedMoment;
	}
	public void setStartedMoment(Date startedMoment) {
		this.startedMoment = startedMoment;
	}
	
	@Min(value = 0)
	@Digits(integer = 9, fraction = 2)
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

	// Relationships ----------------------------------------------------------
	private Collection<Punishment> punishment;
	private Collection<FeePayment> feePayment;

	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "league")
	public Collection<Punishment> getPunishment() {
		return punishment;
	}
	public void setPunishment(Collection<Punishment> punishment) {
		this.punishment = punishment;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "league")
	public Collection<FeePayment> getFeePayment() {
		return feePayment;
	}
	public void setFeePayment(Collection<FeePayment> feePayment) {
		this.feePayment = feePayment;
	}
}
