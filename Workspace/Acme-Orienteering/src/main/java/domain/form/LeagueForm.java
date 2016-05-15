package domain.form;

import java.util.Collection;
import java.util.Date;

import javax.persistence.ElementCollection;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class LeagueForm {

	private String description;
	private String name;
	private Collection<String> pictures;
	private Date startedMoment;
	private double amount;
	private int leagueId;
	private int refereeId;
	
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
	
	@NotNull
	public int getLeagueId() {
		return leagueId;
	}
	public void setLeagueId(int leagueId) {
		this.leagueId = leagueId;
	}
	
	@Valid
	@NotNull
	public int getRefereeId() {
		return refereeId;
	}
	public void setRefereeId(int refereeId) {
		this.refereeId = refereeId;
	}
	
}
