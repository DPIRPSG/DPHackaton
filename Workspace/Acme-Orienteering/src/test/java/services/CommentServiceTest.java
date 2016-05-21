package services;


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
}
