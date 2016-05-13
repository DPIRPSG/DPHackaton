package services.form;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Club;
import domain.form.ClubForm;

import services.ClubService;

@Service
@Transactional
public class ClubFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private ClubService clubService;
	
	// Constructors -----------------------------------------------------------

	public ClubFormService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public ClubForm create() {
		ClubForm result;
		Collection<String> pictures;
		
		pictures = new ArrayList<String>();
		
		result = new ClubForm();
		
		result.setPictures(pictures);
		result.setClubId(0);
		
		return result;
	}
	
	public Club reconstruct(ClubForm clubForm) {
		Club result;
		
		if (clubForm.getClubId() == 0) {
			result = clubService.create();
			result.setName(clubForm.getName());
			result.setDescription(clubForm.getDescription());
			result.setPictures(clubForm.getPictures());
		} else if(clubForm.getClubId() != 0) {			
			result = clubService.findOne(clubForm.getClubId());
			result.setName(clubForm.getName());
			result.setDescription(clubForm.getDescription());
			result.setPictures(clubForm.getPictures());
		} else {
			result = null;
		}
		
		return result;
	}

	public ClubForm findOne(int clubId) {
		ClubForm result;
		Club club;
		
		result = this.create();
		club = clubService.findOne(clubId);
		result.setName(club.getName());
		result.setDescription(club.getDescription());
		result.setPictures(club.getPictures());
		
		return result;
	}

	public void delete(ClubForm clubForm) {
		Club club;
		System.out.println(clubForm.getClubId());
		club = clubService.findOne(clubForm.getClubId());
		clubService.delete(club);
	}
}
