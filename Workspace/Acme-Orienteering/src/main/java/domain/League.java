package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class League extends CommentedEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String description;
	private String name;
	private Collection<String> pictures;
	private Date creationMoment;
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
	//@URL
	public Collection<String> getPictures() {
		return pictures;
	}
	public void setPictures(Collection<String> pictures) {
		this.pictures = pictures;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getCreationMoment() {
		return creationMoment;
	}
	public void setCreationMoment(Date crationMoment) {
		this.creationMoment = crationMoment;
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
	private Collection<Punishment> punishments;
	private Collection<FeePayment> feePayments;
	private Collection<Race> racing;
	private Referee referee;
	private Collection<Finances> finances;

	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "league")
	public Collection<Punishment> getPunishments() {
		return punishments;
	}
	public void setPunishments(Collection<Punishment> punishments) {
		this.punishments = punishments;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "league")
	public Collection<FeePayment> getFeePayments() {
		return feePayments;
	}
	public void setFeePayments(Collection<FeePayment> feePayments) {
		this.feePayments = feePayments;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "league")
	public Collection<Race> getRacing() {
		return racing;
	}
	public void setRacing(Collection<Race> racing) {
		this.racing = racing;
	}
	
	public void addRace(Race race) {
		this.racing.add(race);
	}

	public void removeRace(Race race) {
		this.racing.remove(race);
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Referee getReferee() {
		return referee;
	}
	public void setReferee(Referee referee) {
		this.referee = referee;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy = "league")
	public Collection<Finances> getFinances() {
		return finances;
	}
	public void setFinances(Collection<Finances> finances) {
		this.finances = finances;
	}
	
	
}
