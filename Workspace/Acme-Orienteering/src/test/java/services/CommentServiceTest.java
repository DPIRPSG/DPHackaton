package services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import domain.Club;
import domain.Comment;

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
	
	// Tests ---------------------------------------
	
	/**
	 * 
	 */
	
	/**
	 * 
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
}
