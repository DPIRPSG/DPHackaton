package domain.form;

import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class BarterForm {
	private String title;
	private String offeredName;
	private String offeredDescription;
	private Collection<String> offeredPictures;
	private String requestedName;
	private String requestedDescription;
	private Collection<String> requestedPictures;
	
	@NotBlank
	@NotNull
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@NotBlank
	@NotNull
	public String getOfferedName() {
		return offeredName;
	}
	public void setOfferedName(String offeredName) {
		this.offeredName = offeredName;
	}
	
	@NotBlank
	@NotNull
	public String getOfferedDescription() {
		return offeredDescription;
	}
	public void setOfferedDescription(String offeredDescription) {
		this.offeredDescription = offeredDescription;
	}
	
	@NotNull
	@ElementCollection
	public Collection<String> getOfferedPictures() {
		return offeredPictures;
	}
	public void setOfferedPictures(Collection<String> offeredPictures) {
		this.offeredPictures = offeredPictures;
	}
	
	@NotBlank
	@NotNull
	public String getRequestedName() {
		return requestedName;
	}
	public void setRequestedName(String requestedName) {
		this.requestedName = requestedName;
	}
	
	@NotBlank
	@NotNull
	public String getRequestedDescription() {
		return requestedDescription;
	}
	public void setRequestedDescription(String requestedDescription) {
		this.requestedDescription = requestedDescription;
	}
	
	@NotNull
	@ElementCollection
	public Collection<String> getRequestedPictures() {
		return requestedPictures;
	}
	public void setRequestedPictures(Collection<String> requestedPictures) {
		this.requestedPictures = requestedPictures;
	}
}
