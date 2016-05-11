package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Auditor;

@Repository
public interface AuditorRepository extends JpaRepository<Auditor, Integer> {

	@Query("select a from Auditor a where a.userAccount.id = ?1")
	Auditor findByUserAccountId(int id);
	
	//DASHBOARD
	
	@Query("select a1 from Match m1 left join m1.auditor a1 group by a1 having count(a1) >= all(select count(a2) from Match m2 left join m2.auditor a2 group by a2)")
	Collection<Auditor> getAuditorsWithMoreMatches();
}
