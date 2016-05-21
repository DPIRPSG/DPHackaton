package services;


import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Actor;
import domain.Administrator;
import domain.Curriculum;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CurriculumServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private CurriculumService curriculumService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private AdministratorService administratorService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema
	 * puede crearse un curriculum en condiciones normales
	 */
	@Test
	public void testCreateCurriculumOk() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() == null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() == null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un administrador no
	 * puede crearse un curriculum
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError1() {
		Curriculum curriculum;
		Administrator admin;
		Actor actor;
		
		authenticate("admin");
		admin = administratorService.findByPrincipal();
		
		Assert.isTrue(admin.getCurriculum() == null);
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede crearse un curriculum sin statement
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError2() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() == null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() == null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		//curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede crearse un curriculum sin skills
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError3() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() == null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() == null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Prueba");
		//curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede crearse un curriculum sin likes
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError4() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() == null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() == null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		//curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede crearse un curriculum sin Dislikes
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError5() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() == null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() == null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		//curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede crearse un curriculum sin estar logueado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCurriculumError6() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() == null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() == null);
		
		//authenticate(actor.getUserAccount().getUsername());
		
		curriculum = curriculumService.create();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema
	 * puede editar su curriculum en condiciones normales
	 */
	@Test
	public void testEditCurriculumOk() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() != null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = actor.getCurriculum();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		Assert.isTrue(actor.getCurriculum().getStatement().equals("Prueba"));
		Assert.isTrue(actor.getCurriculum().getSkills().equals("Prueba"));
		Assert.isTrue(actor.getCurriculum().getLikes().equals("Prueba"));
		Assert.isTrue(actor.getCurriculum().getDislikes().equals("Prueba"));
		
		authenticate(null);
	}
	
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede editar su curriculum sin statement
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testEditCurriculumError1() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() != null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = actor.getCurriculum();
		curriculum.setStatement(null);
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede editar su curriculum sin skills
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testEditCurriculumError2() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() != null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = actor.getCurriculum();
		curriculum.setStatement("Prueba");
		curriculum.setSkills(null);
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede editar su curriculum sin likes
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testEditCurriculumError3() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() != null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = actor.getCurriculum();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes(null);
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede editar su curriculum sin Dislikes
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testEditCurriculumError4() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() != null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(actor.getUserAccount().getUsername());
		
		curriculum = actor.getCurriculum();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes(null);
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que un usuario del sistema no
	 * puede editar un curriculum sin estar logueado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testEditCurriculumError5() {
		Curriculum curriculum;
		Collection<Actor> actors;
		Actor actor;
		
		actors = actorService.findAll();
		actor = null;
		
		for(Actor a : actors) {
			if(a.getCurriculum() != null) {
				actor = a;
				break;
			}
		}
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		//authenticate(actor.getUserAccount().getUsername());
		
		curriculum = actor.getCurriculum();
		curriculum.setStatement("Prueba");
		curriculum.setSkills("Prueba");
		curriculum.setLikes("Prueba");
		curriculum.setDislikes("Prueba");
		curriculum = curriculumService.save(curriculum);
		curriculumService.flush();
		
		actor = actorService.findByPrincipal();
		
		Assert.isTrue(actor.getCurriculum() != null);
		
		//authenticate(null);
	}
}
