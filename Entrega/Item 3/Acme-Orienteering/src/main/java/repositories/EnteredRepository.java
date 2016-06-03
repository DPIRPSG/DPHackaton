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
	
	@Query("select e from Entered e where e.club.id = ?1 and e.isMember IS FALSE and e.isDenied IS FALSE and e.acceptedMoment IS NULL")
	Collection<Entered> findAllUnresolvedByClub(int clubId);
	
	@Query("select e from Entered e where e.club.id = ?1 and e.isMember IS TRUE and e.isDenied IS FALSE and e.acceptedMoment IS NOT NULL")
	Collection<Entered> findAllAcceptedByClub(int clubId);
	
	@Query("select e from Entered e where e.club.id = ?1 and e.isMember IS FALSE and e.isDenied IS TRUE and e.acceptedMoment IS NULL")
	Collection<Entered> findAllDeniedByClub(int clubId);
	
	@Query("select e from Entered e where e.club.id = ?1 and e.isMember IS FALSE and e.isDenied IS FALSE and e.acceptedMoment IS NOT NULL")
	Collection<Entered> findAllExpelledByClub(int clubId);
	
}
