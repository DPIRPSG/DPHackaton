package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import domain.Actor;
import domain.Curriculum;

@Service
@Transactional
public class CurriculumService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CurriculumRepository curriculumRepository;
	
	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ActorService actorService;

	// Constructors -----------------------------------------------------------

	public CurriculumService() {
		super();
	}


	// Simple CRUD methods ----------------------------------------------------
	
	public Curriculum create(){
		Assert.isTrue(actorService.checkAuthorities("RUNNER,MANAGER,REFEREE"), "Only a Runner, Manager or Referee can manage his/her Curriculum.");
		
		Curriculum result;		
		
		result = new Curriculum();
				
		return result;
	}
	
	public Curriculum save(Curriculum curriculum){
		Assert.isTrue(actorService.checkAuthorities("RUNNER,MANAGER,REFEREE"), "Only a Runner, Manager or Referee can manage his/her Curriculum.");
		Assert.notNull(curriculum);
		if(curriculum.getId() != 0){ // Si está editando y no creando
			checkCurriculum(curriculum);
		}
		
		Actor actor;
		int curriculumId;
		
		actor = actorService.findByPrincipal();
		curriculumId = curriculum.getId();

		curriculum = curriculumRepository.save(curriculum);
		if(curriculumId == 0){
			actor.setCurriculum(curriculum);
			actorService.save(actor);
		}
		
		return curriculum;
	}
	
	public void delete(Curriculum curriculum){
		Assert.isTrue(actorService.checkAuthorities("RUNNER,MANAGER,REFEREE"), "Only a Runner, Manager or Referee can manage his/her Curriculum.");
		Assert.isTrue(curriculum.getId() != 0, "You cant delete a Curriculum that isn't still saved.");
		checkCurriculum(curriculum);
		
		Actor actor;
		
		actor = actorService.findByPrincipal();
		actor.setCurriculum(null);
		
		curriculumRepository.delete(curriculum);
	}
	
	public Curriculum findOne(int curriculumId) {
		Assert.isTrue(actorService.checkAuthorities("RUNNER,MANAGER,REFEREE"), "Only a Runner, Manager or Referee can manage his/her Curriculum.");
		
		Curriculum result;
		
		result = curriculumRepository.findOne(curriculumId);
		
		return result;
	}
	
	private void checkCurriculum(Curriculum curriculum) {
		Actor actorPrincipal;
		Actor actorOwner;
		int curriculumId;
		
		curriculumId = curriculum.getId();
		actorPrincipal = actorService.findByPrincipal();
		actorOwner = actorService.findByCurriculumId(curriculumId);
		
		Assert.isTrue(actorPrincipal == actorOwner, "You can't manage a Curriculum of other Trainer.");
	}
	
	public void flush() {
		curriculumRepository.flush();
	}
}
