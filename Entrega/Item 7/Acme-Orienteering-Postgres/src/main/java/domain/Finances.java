package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Finances extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private double amount;
	private Date paymentMoment;
	
	@Min(value = 0)
	@Digits(integer = 9, fraction = 2)
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getPaymentMoment() {
		return paymentMoment;
	}
	public void setPaymentMoment(Date paymentMoment) {
		this.paymentMoment = paymentMoment;
	}
	// Relationships ----------------------------------------------------------
	private Sponsor sponsor;
	private League league;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Sponsor getSponsor() {
		return sponsor;
	}
	public void setSponsor(Sponsor sponsor) {
		this.sponsor = sponsor;
	}
	
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public League getLeague() {
		return league;
	}
	public void setLeague(League league) {
		this.league = league;
	}


	
}
