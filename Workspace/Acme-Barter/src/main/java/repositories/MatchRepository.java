package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Integer> {
	
	@Query("select m from Match m where m.cancelled = false and ( m.creatorBarter.user.id = ?1 or  m.receiverBarter.user.id = ?1)")
	Collection<Match> findAllUserInvolves(int userId);
	
	@Query("select m from Match m where m.creatorBarter.user.id = ?1 or m.receiverBarter.user.id = ?1")
	Collection<Match> findAllUserInvolvesIncludeCancelled(int userId);
	
	// Every [barter]match that remains unsigned one month after they were created.
	@Query("select m from Match m where ( m.offerSignsDate = null or m.requestSignsDate = null ) and ( YEAR(m.creationMoment) <= YEAR(CURRENT_DATE) and ( MONTH(CURRENT_DATE) - MONTH(m.creationMoment) >= 1) and DAY(m.creationMoment) <= DAY(CURRENT_DATE) )")
	Collection<Match> findAllNotSignedOneMonthSinceCreation();
	
	@Query("select m from Match m where m.cancelled = false and ( m.creatorBarter.id = ?1 or m.receiverBarter.id = ?1 )")
	Collection<Match> findAllNotCancelledByBarterId(int barterId);
	
	@Query("select distinct m from User a join a.followed u, Match m where a.id=?1 and (m.creatorBarter.user.id = u.id or m.receiverBarter.user.id = u.id) order by m.creationMoment desc")
	Collection<Match> findAllByFollowedUser(int userId);
	
	@Query("select m from Match m where m.auditor.id=?1 order by m.creationMoment desc")
	Collection<Match> findAllByAuditorId(int auditorId);
	
	@Query("select count(c) from Complaint c right join c.match m group by m")
	Collection<Long> getCountOfComplaintsPerMatch();
}
