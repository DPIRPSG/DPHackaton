package services;

import java.util.Collection;
import java.util.Date;

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

import domain.Auditor;
import domain.Barter;
import domain.LegalText;
import domain.Match;
import domain.User;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:spring/datasource.xml",
		"classpath:spring/config/packages.xml"})
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class MatchServiceTest extends AbstractTest {

	// Service under test -------------------------

	@Autowired
	private MatchService matchService;

	// Other services needed -----------------------

	@Autowired
	private BarterService barterService;
	
	@Autowired
	private LegalTextService legalTextService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuditorService auditorService;

	// Tests ---------------------------------------
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que se puede crear un Match en condiciones normales
	 */
	@Test
	public void testCreateMatchOk() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si ningún Barter del Match es del usuario que está logueado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError1() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro Móvil")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro Móvil")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si los dos Barter del Match son del usuario que está logueado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError2() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user2");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro Móvil")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro Móvil")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si algún barter está incluido en un Match no cancelado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError3(){
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("¡¡Intercambio urgente!!")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se crea un Match sin un Barter del usuario logueado, falla
	 */
	@Test(expected=NullPointerException.class)
	@Rollback(value=true)
	public void testCreateMatchError4() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		//match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se crea un Match sin un Barter de otro usuario, falla
	 */
	@Test(expected=NullPointerException.class)
	@Rollback(value=true)
	public void testCreateMatchError5() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		//match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se intenta crear un Match sin un LegalText, falla
	 */
	@Test(expected=ConstraintViolationException.class)
	@Rollback(value=true)
	public void testCreateMatchError6() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		//match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se intenta crear un Match sin estar logueado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError7() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
				
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se intenta crear un Match con un Auditor ya asignado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError8() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		Auditor auditor;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		auditor = auditorService.findAll().iterator().next();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match.setAuditor(auditor);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se intenta crear un Match con un Report ya escrito, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError9() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match.setReport("Test");
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se intenta crear un Match ya firmado por su creador, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError10() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		Date moment;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		moment = new Date();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match.setOfferSignsDate(moment);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.4
	 * An actor who is authenticated as a user must
	 * be able to create a match between two barters.
	 */
	/**
	 * Test que comprueba que si se intenta crear un Match ya firmado por el usuario que recibirá la oferta, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCreateMatchError11() {
		Match match;
		Barter creatorBarter, receiverBarter;
		LegalText legalText;
		Collection<Barter> barters;
		Collection<Match> matches;
		int numMatches;
		Date moment;
		
		barters = barterService.findAll();
		matches = matchService.findAll();
		
		numMatches = matches.size();
		
		moment = new Date();
		
		authenticate("user1");
		
		creatorBarter = null;
		receiverBarter = null;
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}		
		
		legalText = legalTextService.findAll().iterator().next();
				
		match = matchService.create();
		match.setCreatorBarter(creatorBarter);
		match.setReceiverBarter(receiverBarter);
		match.setLegalText(legalText);
		match.setRequestSignsDate(moment);
		match = matchService.save(match);
		
		matches = matchService.findAll();
		
		for(Barter b : barters) {
			if(b.getTitle().equals("Quiero otro PC")) {
				creatorBarter = b;
			} else if(b.getTitle().equals("Quiero otro Portátil")) {
				receiverBarter = b;
			}
		}	
		
		Assert.isTrue(matches.size() == (numMatches+1));
		Assert.isTrue(matches.contains(match));
		Assert.isTrue(creatorBarter.getCreatedMatch().contains(match));
		Assert.isTrue(receiverBarter.getReceivedMatch().contains(match));
		Assert.isTrue(match.getLegalText().equals(legalText));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.5
	 * An actor who is authenticated as a user must be able to
	 * cancel a match as long as he or she is involved in that match.
	 */
	
	/**
	 * Test que comprueba que se puede cancelar un Match en condiciones normales
	 */
	
	@Test
	public void testCancelMatchOk1() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() == null && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == false);
		
		matchService.cancel(match);
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() == null && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == true);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.5
	 * An actor who is authenticated as a user must be able to
	 * cancel a match as long as he or she is involved in that match.
	 */
	
	/**
	 * Test que comprueba que si se intenta cancelar un Match ya cancelado, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCancelMatchError1() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAll();
		
		for(Match m : matches) {
			if(m.getCancelled() == true) {
				if(m.getCreatorBarter().getUser().getId() == user.getId() || m.getReceiverBarter().getUser().getId() == user.getId()) {
					match = m;
					break;
				}
			}
		}
		
		System.out.println(match + " " + match.getId());
		
		Assert.isTrue(match.getCancelled() == true);
		
		matchService.cancel(match);
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.5
	 * An actor who is authenticated as a user must be able to
	 * cancel a match as long as he or she is involved in that match.
	 */
	
	/**
	 * Test que comprueba que si se intenta cancelar un Match en el que el usuario logueado no está involucrado, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCancelMatchError2() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() == null && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		authenticate(null);

		authenticate("user3");
		
		Assert.isTrue(match.getCancelled() == false);
		
		matchService.cancel(match);
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() == null && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == true);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.5
	 * An actor who is authenticated as a user must be able to
	 * cancel a match as long as he or she is involved in that match.
	 */
	
	/**
	 * Test que comprueba que si se intenta cancelar un Match firmado por los dos usuarios, falla
	 */
	
	@Test
	public void testCancelMatchOk2() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() != null && m.getRequestSignsDate() != null) {
				match = m;
				break;
			}
		}
				
		Assert.isTrue(match.getCancelled() == false);
		
		matchService.cancel(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getCancelled() == true);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.5
	 * An actor who is authenticated as a user must be able to
	 * cancel a match as long as he or she is involved in that match.
	 */
	
	/**
	 * Test que comprueba que si se intenta cancelar un Match sin estar logueado, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCancelMatchError4() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() == null && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == false);
		
		authenticate(null);
		
		matchService.cancel(match);
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getOfferSignsDate() == null && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == true);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as a user must be able to
	 * sign a match in which he or she's involved.
	 */
	
	/**
	 * Test que comprueba que el usuario que creó el Match puede firmar la parte que le corresponde
	 */
	
	@Test
	public void testSignMatchOk1() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getCreatorBarter().getUser().getId() == user.getId() && m.getOfferSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == false);
		Assert.isTrue(match.getOfferSignsDate() == null);
		
		matchService.sign(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getOfferSignsDate() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as a user must be able to
	 * sign a match in which he or she's involved.
	 */
	
	/**
	 * Test que comprueba que el usuario que recivió el Match puede firmar la parte que le corresponde
	 */
	
	@Test
	public void testSignMatchOk2() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user2");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getReceiverBarter().getUser().getId() == user.getId() && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == false);
		Assert.isTrue(match.getRequestSignsDate() == null);
		
		matchService.sign(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getRequestSignsDate() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as a user must be able to
	 * sign a match in which he or she's involved.
	 */
	
	/**
	 * Test que comprueba si se intenta firmar un Match sin estar logueado, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testSignMatchError1() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user2");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getReceiverBarter().getUser().getId() == user.getId() && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		authenticate(null);
		
		Assert.isTrue(match.getCancelled() == false);
		Assert.isTrue(match.getRequestSignsDate() == null);
		
		matchService.sign(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getRequestSignsDate() != null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as a user must be able to
	 * sign a match in which he or she's involved.
	 */
	
	/**
	 * Test que comprueba si se intenta firmar un Match en el que no estás involucrado, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testSignMatchError2() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user2");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getReceiverBarter().getUser().getId() == user.getId() && m.getRequestSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		authenticate(null);
		authenticate("user3");
		
		Assert.isTrue(match.getCancelled() == false);
		Assert.isTrue(match.getRequestSignsDate() == null);
		
		matchService.sign(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getRequestSignsDate() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as a user must be able to
	 * sign a match in which he or she's involved.
	 */
	
	/**
	 * Test que comprueba que si se intenta firmar un Match ya firmado anteriormente, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testSignMatchError3() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		for(Match m : matches) {
			if(m.getCreatorBarter().getUser().getId() == user.getId() && m.getOfferSignsDate() != null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == false);
		Assert.isTrue(match.getOfferSignsDate()!= null);
		
		matchService.sign(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getOfferSignsDate() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as a user must be able to
	 * sign a match in which he or she's involved.
	 */
	
	/**
	 * Test que comprueba que si se intenta firmar un Match cancelado, falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testSignMatchError4() {
		Match match;
		Collection<Match> matches;
		User user;
		
		match = null;
		
		authenticate("user1");
		
		user = userService.findByPrincipal();
		
		matches = matchService.findAll();
		
		for(Match m : matches) {
			if(m.getCreatorBarter().getUser().getId() == user.getId() && m.getOfferSignsDate() == null) {
				match = m;
				break;
			}
		}
		
		Assert.isTrue(match.getCancelled() == true);
		Assert.isTrue(match.getOfferSignsDate()== null);
		
		matchService.sign(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getOfferSignsDate() != null);
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as an administrator must be able to
	 * execute a procedure that cancels every barter that remains unsigned one month after they were created.
	 */
	
	/**
	 * Test que comprueba el correcto funcionamiento del algoritmo
	 */
	
	@Test
	public void testProcedureEveryMatchOk() {
		Collection<Match> matches;
		int numCancelledBefore, numCancelledAfter, numMatchesACancelar;
		
		numCancelledAfter = 0;
		numCancelledBefore = 0;
		numMatchesACancelar = 0;
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledBefore++;
			}
		}
		
		authenticate("admin");
		
		matches = matchService.findAllNotSignedOneMonthSinceCreation();
		for(Match m : matches) {
			if(!m.getCancelled()) {
				numMatchesACancelar++;
			}
		}
		
		matchService.cancelEveryMatchNotSignedOneMonthSinceCreation();
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledAfter++;
			}
		}

		Assert.isTrue(numCancelledAfter == (numCancelledBefore + numMatchesACancelar));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as an administrator must be able to
	 * execute a procedure that cancels every barter that remains unsigned one month after they were created.
	 */
	
	/**
	 * Test que comprueba que si no lo ejecuta un administrador falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testProcedureEveryMatchError1() {
		Collection<Match> matches;
		int numCancelledBefore, numCancelledAfter;
		
		numCancelledAfter = 0;
		numCancelledBefore = 0;
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledBefore++;
			}
		}
				
		matchService.cancelEveryMatchNotSignedOneMonthSinceCreation();
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledAfter++;
			}
		}
		
		Assert.isTrue(numCancelledAfter == (numCancelledBefore + 1));
		
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as an administrator must be able to
	 * execute a procedure that cancels every barter that remains unsigned one month after they were created.
	 */
	
	/**
	 * Test que comprueba que si no lo ejecuta un administrador falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testProcedureEveryMatchError2() {
		Collection<Match> matches;
		int numCancelledBefore, numCancelledAfter;
		
		numCancelledAfter = 0;
		numCancelledBefore = 0;
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledBefore++;
			}
		}
		
		authenticate("user1");
				
		matchService.cancelEveryMatchNotSignedOneMonthSinceCreation();
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledAfter++;
			}
		}
		
		Assert.isTrue(numCancelledAfter == (numCancelledBefore + 1));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Barter - Level C - 11.6
	 * An actor who is authenticated as an administrator must be able to
	 * execute a procedure that cancels every barter that remains unsigned one month after they were created.
	 */
	
	/**
	 * Test que comprueba que si no lo ejecuta un administrador falla
	 */
	
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testProcedureEveryMatchError3() {
		Collection<Match> matches;
		int numCancelledBefore, numCancelledAfter;
		
		numCancelledAfter = 0;
		numCancelledBefore = 0;
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledBefore++;
			}
		}
		
		authenticate("auditor1");
				
		matchService.cancelEveryMatchNotSignedOneMonthSinceCreation();
		
		matches = matchService.findAll();
		
		for(Match match : matches) {
			if(match.getCancelled()) {
				numCancelledAfter++;
			}
		}
		
		Assert.isTrue(numCancelledAfter == (numCancelledBefore + 1));
		
		authenticate(null);
	}
	
	/**
	 * Acme-Six-Pack - Level B - 3.4
	 * An actor who is authenticated a user must be able to:
	 * Display a stream of bulletins in which the system provides information about the barters that the users that he or she follows have created.
	 * 
	 * Positive test case: Muestra la información pertinente.
	 * 
	 */
	@Test
	public void testMatchFindAllBulletin1(){
		// Declare variable
		Collection<Match> result;
		
		// Load objects to test
		authenticate("user1");
		
		// Check basic requirements
		
		// Execution of test
		result = matchService.findAllByFollowedUser();
		
		// Check results
		Assert.isTrue(result.size() == 6);
		authenticate(null);
		barterService.flush();
	}
	
	/**
	 * Acme-Six-Pack - Level B - 3.4
	 * An actor who is authenticated a user must be able to:
	 * Display a stream of bulletins in which the system provides information about the barters that the users that he or she follows have created.
	 * 
	 * Negative test case: No muestra la información pertinente ya que no eres un user.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value = true) 
	public void testMatchFindAllBulletin2(){
		// Declare variable
		Collection<Match> result;
		
		// Load objects to test
		//authenticate("user1");
		
		// Check basic requirements
		
		// Execution of test
		result = matchService.findAllByFollowedUser();
		
		// Check results
		Assert.isTrue(result.size() == 6);
		//authenticate(null);
		barterService.flush();
	}
	
	@Test
	public void testFindAllUserInvolves(){
		Collection<Match> matches;
		User user;
		
		authenticate("user1");
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolves(user.getId());
		
		Assert.isTrue(matches.size() == 3);
		
		authenticate(null);
	}
	
	@Test
	public void testFindAllUserInvolvesIncludeCancelled(){
		Collection<Match> matches;
		User user;
		
		authenticate("user3");
		user = userService.findByPrincipal();
		
		matches = matchService.findAllUserInvolvesIncludeCancelled(user.getId());
		
		Assert.isTrue(matches.size() == 3);
		
		authenticate(null);
	}
	
	@Test
	public void testFindAllNotSignedOneMonthSinceCreation(){
		Collection<Match> matches;
		
		authenticate("admin");
		
		matches = matchService.findAllNotSignedOneMonthSinceCreation();
		System.out.println(matches.size());
		Assert.isTrue(matches.size() == 4);
		
		authenticate(null);
	}
	
	/**
	 * An actor who is authenticated as an administrator must be able to
	 * close a barter or a match so that no more complaints
	 * can be created regarding it. The system must record
	 * the administrator who closed a barter.
	 */
	/**
	 * Test que comprueba que un admin puede cerrar un Match
	 */
	@Test
	public void testCloseMatchOk() {
		Collection<Match> matches;
		Match match;
		
		matches = matchService.findAll();
		match = null;
		
		for(Match m : matches) {
			if(m.getClosed() == false) {
				match = m;
				break;
			}
		}
		Assert.isTrue(match.getClosed() == false);
		
		authenticate("admin");
		
		matchService.close(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getClosed() == true);
		
		authenticate(null);
	}
	
	/**
	 * An actor who is authenticated as an administrator must be able to
	 * close a barter or a match so that no more complaints
	 * can be created regarding it. The system must record
	 * the administrator who closed a barter.
	 */
	/**
	 * Test que comprueba que solo un admin puede cerrar un Matcth
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCloseMatchError1() {
		Collection<Match> matches;
		Match match;
		
		matches = matchService.findAll();
		match = null;
		
		for(Match m : matches) {
			if(m.getClosed() == false) {
				match = m;
				break;
			}
		}
		Assert.isTrue(match.getClosed() == false);
				
		matchService.close(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getClosed() == true);
	}
	
	/**
	 * An actor who is authenticated as an administrator must be able to
	 * close a barter or a match so that no more complaints
	 * can be created regarding it. The system must record
	 * the administrator who closed a barter.
	 */
	/**
	 * Test que comprueba que si se intenta cerrar un Match ya cerrado, falla
	 */
	@Test(expected=IllegalArgumentException.class)
	@Rollback(value=true)
	public void testCloseMatchError2() {
		Collection<Match> matches;
		Match match;
		
		matches = matchService.findAll();
		match = null;
		
		for(Match m : matches) {
			if(m.getClosed() == true) {
				match = m;
				break;
			}
		}
		Assert.isTrue(match.getClosed() == true);
		
		authenticate("admin");
		
		matchService.close(match);
		
		match = matchService.findOne(match.getId());
		
		Assert.isTrue(match.getClosed() == true);
		
		authenticate(null);
	}
}
