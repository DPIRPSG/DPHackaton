package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Entered;

@Repository
public interface EnteredRepository extends JpaRepository<Entered, Integer> {

	@Query("select e from Entered e where e.runner.id = ?1")
	Collection<Entered> findAllByRunner(int runnerId);
	
	@Query("select e from Entered e where e.club.id = ?1")
	Collection<Entered> findAllByClub(int clubId);
	
	@Query("select e from Entered e where e.club.id = ?1 and e.isMember IS FALSE and e.registerMoment IS NULL and e.acceptedMoment IS NULL")
	Collection<Entered> findAllUnresolvedByClub(int clubId);
	
}
