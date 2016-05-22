package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Race;

@Repository
public interface RaceRepository extends JpaRepository<Race, Integer> {

	@Query("select r from Race r join r.classifications c where c.club.id = ?1")
	Collection<Race> findAllByClubId(Integer clubId);
	
	@Query("select r from Race r left join r.participates p where p.runner.id = ?1")
	Collection<Race> findAllByRunnerId(int runnerId);
	
	// DASHBOARD
	
	@Query("select (count(distinct r1)*1.0)/(count(distinct l1)*1.0) from Race r1 left join r1.league l1")
	Double ratioOfRacesByLeague();

}
