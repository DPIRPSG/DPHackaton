package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Entered extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String report;
	private boolean isMember;
	private Date registerMoment;
	private Date acceptedMoment;
	
	@NotBlank
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	
	public boolean getIsMember() {
		return isMember;
	}
	public void setIsMember(boolean isMember) {
		this.isMember = isMember;
	}
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getRegisterMoment() {
		return registerMoment;
	}
	public void setRegisterMoment(Date registerMoment) {
		this.registerMoment = registerMoment;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	public Date getAcceptedMoment() {
		return acceptedMoment;
	}
	public void setAcceptedMoment(Date acceptedMoment) {
		this.acceptedMoment = acceptedMoment;
	}
	
	// Relationships ----------------------------------------------------------
	private Runner runner;
	private Club club;

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Runner getRunner() {
		return runner;
	}
	public void setRunner(Runner runner) {
		this.runner = runner;
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
