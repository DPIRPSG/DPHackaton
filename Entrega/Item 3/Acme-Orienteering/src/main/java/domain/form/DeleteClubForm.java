package domain.form;

import javax.validation.constraints.NotNull;

public class DeleteClubForm {

	private int clubId;
	private int managerId;
	
	
	@NotNull
	public int getClubId() {
		return clubId;
	}
	public void setClubId(int clubId) {
		this.clubId = clubId;
	}
	
	@NotNull
	public int getManagerId() {
		return managerId;
	}
	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
}
