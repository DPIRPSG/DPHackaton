package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {

	@Query("select c from Club c join c.feePayments f where f.league.id = ?1")
	Collection<Club> findAllByLeagueId(Integer leagueId);

	@Query("select c from Club c join c.entered e where e.runner.id = ?1 and e.isMember = true")
	Club findOneByRunnerId(int id);

}
