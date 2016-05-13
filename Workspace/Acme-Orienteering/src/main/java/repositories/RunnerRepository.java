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
	
}
