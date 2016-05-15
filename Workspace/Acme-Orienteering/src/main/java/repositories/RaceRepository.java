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

}
