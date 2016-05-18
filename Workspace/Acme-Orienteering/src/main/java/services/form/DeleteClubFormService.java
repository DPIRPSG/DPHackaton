package services.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Club;
import domain.form.DeleteClubForm;

import services.ClubService;

@Service
@Transactional
public class DeleteClubFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	// Constructors -----------------------------------------------------------

	public DeleteClubFormService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	private DeleteClubForm create() {
		DeleteClubForm result;
				
		result = new DeleteClubForm();
				
		return result;
	}
	

	public DeleteClubForm findOne(int clubId) {
		DeleteClubForm result;
		
		result = this.create();
		result.setClubId(clubId);
		
		return result;
	}

	public void delete(DeleteClubForm clubForm) {
		Club club;
		club = clubService.findOne(clubForm.getClubId());
		clubService.delete(club, clubForm.getManagerId());
	}
}
