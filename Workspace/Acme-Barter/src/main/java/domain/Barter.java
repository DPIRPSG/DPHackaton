package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = { @Index(columnList = "cancelled"), @Index(columnList = "title") })
public class Barter extends DomainEntity{

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String title;
	private boolean cancelled;
	private Date registerMoment;
	private boolean closed;

	@NotBlank
	@NotNull
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
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
	
	public boolean getClosed() {
		return closed;
	}
	public void setClosed(boolean closed) {
		this.closed = closed;
	}
	
	// Relationships ----------------------------------------------------------
	private User user;
	private Administrator administrator;
	private Item offered;
	private Item requested;
	private Collection<Match> createdMatch;
	private Collection<Match> receivedMatch;
	private Collection<Barter> relatedBarter;

	@Valid
	@NotNull
	@ManyToOne(optional=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Valid
	@ManyToOne(optional=true)
	public Administrator getAdministrator() {
		return administrator;
	}
	public void setAdministrator(Administrator admin) {
		this.administrator = admin;
	}
	
	@Valid
	@NotNull
	@OneToOne(optional=false)
	public Item getOffered() {
		return offered;
	}
	public void setOffered(Item offered) {
		this.offered = offered;
	}
	
	@Valid
	@NotNull
	@OneToOne(optional=false)	
	public Item getRequested() {
		return requested;
	}
	public void setRequested(Item requested) {
		this.requested = requested;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy="creatorBarter")
	public Collection<Match> getCreatedMatch() {
		return createdMatch;
	}
	public void setCreatedMatch(Collection<Match> createdMatch) {
		this.createdMatch = createdMatch;
	}
	
	@Valid
	@NotNull
	@OneToMany(mappedBy="receiverBarter")
	public Collection<Match> getReceivedMatch() {
		return receivedMatch;
	}
	public void setReceivedMatch(Collection<Match> receivedMatch) {
		this.receivedMatch = receivedMatch;
	}
	
	/**
	 * NO USAR ! ! ! usar barterService.getRelatedBarter
	 * @return
	 */
	@Valid
	@NotNull
	@ManyToMany
	public Collection<Barter> getRelatedBarter() {
		return relatedBarter;
	}
	public void setRelatedBarter(Collection<Barter> relatedBarter) {
		this.relatedBarter = relatedBarter;
	}
	
	

}
