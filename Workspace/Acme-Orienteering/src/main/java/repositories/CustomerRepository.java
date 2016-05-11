package repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import domain.Runner;

@Repository
public interface CustomerRepository extends JpaRepository<Runner, Integer> {
	
	@Query("select c from Customer c where c.userAccount.id = ?1")
	Runner findByUserAccountId(int userAccountId);
	
}
