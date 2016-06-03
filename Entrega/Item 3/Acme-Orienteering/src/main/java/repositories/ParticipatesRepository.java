package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Participates;

@Repository
public interface ParticipatesRepository extends JpaRepository<Participates, Integer> {

//	@Query("select p from Participates p where p.runner.id = ?1 and p.race.id = ?2 and p.referee.id = ?3")
	@Query("select distinct(p) from Participates p join p.runner.entered e where" +
			" (?1 < 0 or p.runner.id = ?1) and" +
			" (?2 < 0 or p.race.id = ?2) and" +
			" (?3 < 0 or p.race.league.referee.id = ?3) and " +
			" (?4 < 0 or (e.club.id = ?4 and e.isMember = true))")
	Collection<Participates> findAllByRunnerIdRaceIdAndRefereeId(int runnerId, int raceId, int refereeId, int clubId);

	@Query("select p from Participates p where p.race.id = ?1")
	Collection<Participates> findAllByRaceId(int raceId);

}
