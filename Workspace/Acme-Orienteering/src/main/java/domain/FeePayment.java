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
public class FeePayment extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private CreditCard creditCard;
	private double amount;
	private Date paymentMoment;
	
	@Valid
	@NotNull
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	
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
