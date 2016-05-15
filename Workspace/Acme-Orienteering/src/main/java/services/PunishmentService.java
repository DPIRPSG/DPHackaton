package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import domain.Punishment;

import repositories.PunishmentRepository;

@Service
@Transactional
public class PunishmentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PunishmentRepository punishmentRepository;

	// Supporting services ----------------------------------------------------
	
	// Constructors -----------------------------------------------------------

	public PunishmentService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	
	public Collection<Punishment> findAll() {
		Collection<Punishment> result;

		result = punishmentRepository.findAll();

		return result;
	}
	

	// Other business methods -------------------------------------------------
	
	public void flush() {
		punishmentRepository.flush();
	}

	public Collection<Punishment> findAllByClubId(Integer clubId) {
		Collection<Punishment> result;

		result = punishmentRepository.findAllByClubId(clubId);

		return result;
	}
}
