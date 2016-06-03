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
	
	@Query("select c from Club c join c.entered e where e.runner.id = ?1")
	Collection<Club> findAllByRunner(int runnerId);
	
	// DASHBOARD
	
	@Query("select c1 from Club c1 join c1.classifications cl1 where cl1.position = 1 group by c1 having count(cl1) >= all(select count(cl2) from Club c2 join c2.classifications cl2 where cl2.position = 1 group by cl2)")
	Collection<Club> findAllWhoHaveWonMoreRaces();
	
	@Query("select c1 from Club c1 left join c1.entered e1 where e1.isMember IS FALSE AND e1.isDenied IS TRUE AND e1.acceptedMoment IS NULL group by c1 having count(e1) >= all(select count(e2) from Club c2 left join c2.entered e2 where e2.isMember IS FALSE AND e2.isDenied IS TRUE AND e2.acceptedMoment IS NULL group by c2)")
	Collection<Club> findAllWhoHaveMoreDeniedEntered();
	
	@Query("select c1 from Club c1 left join c1.punishments p1 group by c1 having count(p1) >= all(select count(p2) from Club c2 left join c2.punishments p2 group by c2)")
	Collection<Club> findAllWhoHaveMorePunishments();
	
	@Query("select (count(distinct f1)*1.0)/(count(distinct l1)*1.0) from Club c1, League l1 left join c1.feePayments f1")
	Double ratioOfClubsByLeague();
	
	@Query("select c1 from Club c1 join c1.classifications cl1 group by c1 having sum(cl1.points) >= all(select sum(cl1.points) from Club c1 join c1.classifications cl1 group by c1)")
	Collection<Club> findAllWithMorePoints();
	
	@Query("select c1 from Club c1 join c1.classifications cl1 group by c1 having sum(cl1.points) <= all(select sum(cl1.points) from Club c1 join c1.classifications cl1 group by c1)")
	Collection<Club> findAllWithLessPoint();

}
