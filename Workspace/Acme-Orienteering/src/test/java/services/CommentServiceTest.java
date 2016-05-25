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

import domain.Club;
import domain.Comment;
import domain.League;
import domain.Race;
import domain.Runner;

import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class CommentServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private CommentService commentService;
	
	// Other services needed -----------------------
	
	@Autowired
	private ClubService clubService;
	
	@Autowired
	private LeagueService leagueService;
	
	@Autowired
	private RaceService raceService;
	
	@Autowired
	private RunnerService runnerService;
	
	// Tests ---------------------------------------
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede crear un comentario
	 * sobre un club sin problemas en condiciones normales
	 */
	@Test
	public void testCreateCommentOk1() {
		Comment comment;
		Club club;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		club = clubService.findAll().iterator().next();
		entityId = club.getId();
		numCommentsPre = club.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
			
		club = clubService.findOne(entityId);
		numCommentsPost = club.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(club.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede crear un comentario
	 * sobre una liga sin problemas en condiciones normales
	 */
	@Test
	public void testCreateCommentOk2() {
		Comment comment;
		League league;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		league = leagueService.findAll().iterator().next();
		entityId = league.getId();
		numCommentsPre = league.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
			
		league = leagueService.findOne(entityId);
		numCommentsPost = league.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(league.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede crear un comentario
	 * sobre una liga sin problemas en condiciones normales
	 */
	@Test
	public void testCreateCommentOk3() {
		Comment comment;
		Race race;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		race = raceService.findAll().iterator().next();
		entityId = race.getId();
		numCommentsPre = race.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
			
		race = raceService.findOne(entityId);
		numCommentsPost = race.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(race.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre un club sin texto
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentError1() {
		Comment comment;
		Club club;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		club = clubService.findAll().iterator().next();
		entityId = club.getId();
		numCommentsPre = club.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		//comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
		commentService.flush();
			
		club = clubService.findOne(entityId);
		numCommentsPost = club.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(club.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una liga sin texto
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentError2() {
		Comment comment;
		League league;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		league = leagueService.findAll().iterator().next();
		entityId = league.getId();
		numCommentsPre = league.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		//comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
		commentService.flush();
			
		league = leagueService.findOne(entityId);
		numCommentsPost = league.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(league.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una liga sin texto
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateCommentError3() {
		Comment comment;
		Race race;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		race = raceService.findAll().iterator().next();
		entityId = race.getId();
		numCommentsPre = race.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		//comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
		commentService.flush();
			
		race = raceService.findOne(entityId);
		numCommentsPost = race.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(race.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre un club sin estar logueado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError4() {
		Comment comment;
		Club club;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		club = clubService.findAll().iterator().next();
		entityId = club.getId();
		numCommentsPre = club.getComments().size();
		
		//authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
		commentService.flush();
			
		club = clubService.findOne(entityId);
		numCommentsPost = club.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(club.getComments().contains(comment));
		
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una liga sin estar logueado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError5() {
		Comment comment;
		League league;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		league = leagueService.findAll().iterator().next();
		entityId = league.getId();
		numCommentsPre = league.getComments().size();
		
		//authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
		commentService.flush();
			
		league = leagueService.findOne(entityId);
		numCommentsPost = league.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(league.getComments().contains(comment));
		
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una liga sin estar logueado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError6() {
		Comment comment;
		Race race;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		race = raceService.findAll().iterator().next();
		entityId = race.getId();
		numCommentsPre = race.getComments().size();
		
		//authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment = commentService.save(comment);
		commentService.flush();
			
		race = raceService.findOne(entityId);
		numCommentsPost = race.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(race.getComments().contains(comment));
		
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre un club con el atributo deleted a true
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError7() {
		Comment comment;
		Club club;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		club = clubService.findAll().iterator().next();
		entityId = club.getId();
		numCommentsPre = club.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment.setDeleted(true);
		comment = commentService.save(comment);
			
		club = clubService.findOne(entityId);
		numCommentsPost = club.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(club.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una liga con el atributo deleted a true
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError8() {
		Comment comment;
		League league;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		league = leagueService.findAll().iterator().next();
		entityId = league.getId();
		numCommentsPre = league.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment.setDeleted(true);
		comment = commentService.save(comment);
			
		league = leagueService.findOne(entityId);
		numCommentsPost = league.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(league.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una carrera con el atributo deleted a true
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError9() {
		Comment comment;
		Race race;
		int entityId;
		int numCommentsPre, numCommentsPost;
		
		race = raceService.findAll().iterator().next();
		entityId = race.getId();
		numCommentsPre = race.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment.setDeleted(true);
		comment = commentService.save(comment);
			
		race = raceService.findOne(entityId);
		numCommentsPost = race.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(race.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre un club en nombre de otro usuario
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError10() {
		Comment comment;
		Club club;
		int entityId;
		int numCommentsPre, numCommentsPost;
		Runner runner;
		
		authenticate("runner2");
		runner = runnerService.findByPrincipal();
		authenticate(null);
		
		club = clubService.findAll().iterator().next();
		entityId = club.getId();
		numCommentsPre = club.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment.setActor(runner);
		comment = commentService.save(comment);
			
		club = clubService.findOne(entityId);
		numCommentsPost = club.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(club.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una liga en nombre de otro usuario
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError11() {
		Comment comment;
		League league;
		int entityId;
		int numCommentsPre, numCommentsPost;
		Runner runner;
		
		authenticate("runner2");
		runner = runnerService.findByPrincipal();
		authenticate(null);
		
		league = leagueService.findAll().iterator().next();
		entityId = league.getId();
		numCommentsPre = league.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment.setActor(runner);
		comment = commentService.save(comment);
			
		league = leagueService.findOne(entityId);
		numCommentsPost = league.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(league.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede crear un comentario
	 * sobre una carrera en nombre de otro usuario
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateCommentError12() {
		Comment comment;
		Race race;
		int entityId;
		int numCommentsPre, numCommentsPost;
		Runner runner;
		
		authenticate("runner2");
		runner = runnerService.findByPrincipal();
		authenticate(null);
		
		race = raceService.findAll().iterator().next();
		entityId = race.getId();
		numCommentsPre = race.getComments().size();
		
		authenticate("runner1");
		comment = commentService.create(entityId);
		comment.setText("Prueba");
		comment.setStarRating(3);
		comment.setActor(runner);
		comment = commentService.save(comment);
			
		race = raceService.findOne(entityId);
		numCommentsPost = race.getComments().size();
				
		Assert.isTrue((numCommentsPre + 1) == numCommentsPost);
		Assert.isTrue(race.getComments().contains(comment));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre un club en condiciones normales
	 */
	@Test
	public void testDeleteCommentOk1() {
		Collection<Club> clubes;
		Club club;
		Comment comment;
		
		clubes = clubService.findAll();
		club = null;
		comment = null;
		
		for(Club c : clubes) {
			if(!c.getComments().isEmpty()) {
				club = c;
				break;
			}
		}
				
		authenticate("admin");		
		
		for(Comment c : club.getComments()) {
			if(!c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre una liga en condiciones normales
	 */
	@Test
	public void testDeleteCommentOk2() {
		Collection<League> leagues;
		League league;
		Comment comment;
		
		leagues = leagueService.findAll();
		league = null;
		comment = null;
		
		for(League l : leagues) {
			if(!l.getComments().isEmpty()) {
				league = l;
				break;
			}
		}
				
		authenticate("admin");		
		
		for(Comment c : league.getComments()) {
			if(!c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre una carrera en condiciones normales
	 */
	@Test
	public void testDeleteCommentOk3() {
		Collection<Race> races;
		Race race;
		Comment comment;
		
		races = raceService.findAll();
		race = null;
		comment = null;
		
		for(Race r : races) {
			if(!r.getComments().isEmpty()) {
				race = r;
				break;
			}
		}
				
		authenticate("admin");		
		
		for(Comment c : race.getComments()) {
			if(!c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede borrar un comentario
	 * sobre un club sin ser administrador
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCommentError1() {
		Collection<Club> clubes;
		Club club;
		Comment comment;
		
		clubes = clubService.findAll();
		club = null;
		comment = null;
		
		for(Club c : clubes) {
			if(!c.getComments().isEmpty()) {
				club = c;
				break;
			}
		}
				
		//authenticate("admin");		
		
		for(Comment c : club.getComments()) {
			if(!c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre una liga sin ser administrador
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCommentError2() {
		Collection<League> leagues;
		League league;
		Comment comment;
		
		leagues = leagueService.findAll();
		league = null;
		comment = null;
		
		for(League l : leagues) {
			if(!l.getComments().isEmpty()) {
				league = l;
				break;
			}
		}
				
		//authenticate("admin");		
		
		for(Comment c : league.getComments()) {
			if(!c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre una carrera sin ser administrador
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCommentError3() {
		Collection<Race> races;
		Race race;
		Comment comment;
		
		races = raceService.findAll();
		race = null;
		comment = null;
		
		for(Race r : races) {
			if(!r.getComments().isEmpty()) {
				race = r;
				break;
			}
		}
				
		//authenticate("admin");		
		
		for(Comment c : race.getComments()) {
			if(!c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == false);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		//authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que no se puede borrar un comentario
	 * sobre un club si el comentario que se intenta borrar ya esta borrado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCommentError4() {
		Collection<Club> clubes;
		Club club;
		Comment comment;
		
		clubes = clubService.findAll();
		club = null;
		comment = null;
		
		for(Club c : clubes) {
			if(!c.getComments().isEmpty()) {
				club = c;
				break;
			}
		}
				
		authenticate("admin");		
		
		for(Comment c : club.getComments()) {
			if(c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == true);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre una liga si el comentario que se intenta borrar ya esta borrado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCommentError5() {
		Collection<League> leagues;
		League league;
		Comment comment;
		
		leagues = leagueService.findAll();
		league = null;
		comment = null;
		
		for(League l : leagues) {
			if(!l.getComments().isEmpty()) {
				league = l;
				break;
			}
		}
				
		authenticate("admin");		
		
		for(Comment c : league.getComments()) {
			if(c.getDeleted()) {
				comment = c;
				break;
			}
		}
		Assert.isTrue(comment.getDeleted() == true);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que se puede borrar un comentario
	 * sobre una carrera si el comentario que se intenta borrar ya esta borrado
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testDeleteCommentError6() {
		Collection<Race> races;
		Race race;
		Comment comment;
		
		races = raceService.findAll();
		race = null;
		comment = null;
		
		for(Race r : races) {
			if(!r.getComments().isEmpty()) {
				race = r;
				break;
			}
		}
				
		authenticate("admin");		
		
		for(Comment c : race.getComments()) {
			if(c.getDeleted()) {
				comment = c;
				break;
			}
		}
		
		Assert.isTrue(comment.getDeleted() == true);
		
		commentService.delete(comment);
		
		comment = commentService.findOne(comment.getId());		
		Assert.isTrue(comment.getDeleted() == true);
		authenticate(null);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que el listado de los comentarios
	 * de un club funciona correctamente
	 */
	@Test
	public void testFindAllByCommentedEntityId1() {
		Collection<Comment> comments;
		Club club;
		int commentedEntityId;
		
		club = clubService.findAll().iterator().next();
		commentedEntityId = club.getId();
		
		comments = commentService.findAllByCommentedEntityId(commentedEntityId);
		
		Assert.isTrue(comments.size() == 6);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que el listado de los comentarios
	 * de una liga funciona correctamente
	 */
	@Test
	public void testFindAllByCommentedEntityId2() {
		Collection<Comment> comments;
		League league;
		int commentedEntityId;
		
		league = leagueService.findAll().iterator().next();
		commentedEntityId = league.getId();
		
		comments = commentService.findAllByCommentedEntityId(commentedEntityId);
		
		Assert.isTrue(comments.size() == 1);
	}
	
	/**
	 * Acme-Orienteering - 
	 */
	
	/**
	 * Test que comprueba que el listado de los comentarios
	 * de una carrera funciona correctamente
	 */
	@Test
	public void testFindAllByCommentedEntityId3() {
		Collection<Comment> comments;
		Race race;
		int commentedEntityId;
		
		race = raceService.findAll().iterator().next();
		commentedEntityId = race.getId();
		
		comments = commentService.findAllByCommentedEntityId(commentedEntityId);
		
		Assert.isTrue(comments.size() == 1);
	}
}
