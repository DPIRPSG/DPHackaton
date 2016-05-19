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

}
