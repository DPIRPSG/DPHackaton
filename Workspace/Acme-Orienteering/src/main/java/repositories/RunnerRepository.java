package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import domain.Runner;

@Repository
public interface RunnerRepository extends JpaRepository<Runner, Integer> {
	
	@Query("select r from Runner r where r.userAccount.id = ?1")
	Runner findByUserAccountId(int userAccountId);

	@Query("select r from Runner r join r.entered e where e.club.id = ?1 and e.isMember = true")
	Collection<Runner> findAllByClubId(int clubId);
	
	@Query("select ru from Race r left join r.league l left join l.feePayments f left join f.club c left join c.entered e left join e.runner ru where r.id = ?1 and e.isMember IS TRUE and e.acceptedMoment IS NOT NULL group by ru")
	Collection<Runner> findAllWhoCanJoinARace(int raceId);
	
}
