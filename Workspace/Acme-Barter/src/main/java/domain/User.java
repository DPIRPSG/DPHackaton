package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	
	// Relationships ----------------------------------------------------------
	
	private Collection<User> followed;
	
	@Valid
	@NotNull
	@ManyToMany
	public Collection<User> getFollowed() {
		return followed;
	}
	public void setFollowed(Collection<User> followed) {
		this.followed = followed;
	}
	public void addFollowed(User user){
		followed.add(user);
	}
	public void removeFollowed(User user){
		followed.remove(user);
	}
	
}
