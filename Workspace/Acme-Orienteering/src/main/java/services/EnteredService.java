package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import domain.Entered;

import repositories.EnteredRepository;

@Service
@Transactional
public class EnteredService {
	
	// Managed repository -----------------------------------------------------
	
	@Autowired
	private EnteredRepository enteredRepository;
	
	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	
	public EnteredService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public Collection<Entered> findAllByRunner(int runnerId){
		Collection<Entered> result;
		
		result = enteredRepository.findAllByRunner(runnerId);
		
		return result;
	}
	
	public Collection<Entered> findAllByClub(int clubId){
		Collection<Entered> result;
		
		result = enteredRepository.findAllByClub(clubId);
		
		return result;
	}
	
	public Collection<Entered> findAllToReviseByClub(int clubId){
		Collection<Entered> result;
		
		result = enteredRepository.findAllToReviseByClub(clubId);
		
		return result;
	}

}
