package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, Integer> {

	@Query("select l from League l join l.feePayments f where f.club.id = ?1")
	Collection<League> findAllByClubId(Integer clubId);
	
	@Query("select f.league from FeePayment f where f.league.referee.id = ?1 and f.club.id = ?2")
	Collection<League> findAllByRefereeAndClubId(Integer refereeId, Integer clubId);
	
	// DASHBOARD
	
	@Query("select l1 from League l1 left join l1.racing r1 group by l1 having count(r1) >= all(select count(r2) from League l2 left join l2.racing r2 group by l2)")
	Collection<League> findAllWhoHaveMoreRaces();
	
	@Query("select l1 from League l1 left join l1.feePayments f1 left join f1.club c1 group by l1 having count(c1) >= all(select count(c2) from League l2 left join l2.feePayments f2 left join f2.club c2 group by l2)")
	Collection<League> findAllWhoHaveMoreClubs();
}
