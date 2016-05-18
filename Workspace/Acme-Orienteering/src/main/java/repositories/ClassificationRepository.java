package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Classification;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer> {

	@Query("select distinct(p) from Classification p where" +
			" (?1 < 0 or p.club.id = ?1) and" +
			" (?2 < 0 or p.race.id = ?2)")
	Collection<Classification> findAllByClubIdAndRaceId(int clubId, int raceId);
}
