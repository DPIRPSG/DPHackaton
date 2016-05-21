package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CommentedEntityRepository;
import domain.CommentedEntity;

@Service
@Transactional
public class CommentedEntityService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CommentedEntityRepository commentedEntityRepository;
	
	// Supporting services ----------------------------------------------------

	@Autowired
	private ActorService actorService;
	
	// Constructors -----------------------------------------------------------
	
	public CommentedEntityService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------

	public CommentedEntity findOne(int commentedEntityId){
		CommentedEntity result;
		
		result = commentedEntityRepository.findOneById(commentedEntityId);
		
		return result;
	}

	public void save(CommentedEntity commentedEntity) {
		Assert.notNull(commentedEntity);
		Assert.isTrue(actorService.checkLogin(), "Only an authenticated user can save comments");
	
		commentedEntityRepository.save(commentedEntity);
	}
	
	// Other business methods -------------------------------------------------
	
}
