package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Complaint;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
	
	@Query("select c from Complaint c where c.barter.id = ?1 or c.match.id = ?1")
	Collection<Complaint> findAllByBarterOrMatch(int barterOrMatchId);
	
	@Query("select count(c) from Complaint c")
	Integer getTotalNumberOfComplaintsCreated();
	
	@Query("select c from Complaint c where c.user.id = ?1")
	Collection<Complaint> findAllByUserId(int userId);

}
