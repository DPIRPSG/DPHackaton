package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	// DASHBOARD
	
	@Query("select c1 from Race r1 left join r1.category c1 group by c1 having count(c1) >= all(select count(c2) from Race r2 left join r2.category c2 group by c2)")
	Collection<Category> findAllMostFrequentInRaces();

}
