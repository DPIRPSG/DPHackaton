package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {

	@Query("select c from Club c where c.manager.id = ?1")
	Club findByManagerId(int id);

}
