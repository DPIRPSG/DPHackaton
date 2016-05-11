package repositories;


import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("select u from User u where u.userAccount.id = ?1")
	User findByUserAccountId(int userAccountId);	
	
	@Query("select u from User u join u.followed f where f.id = ?1")
	Collection<User> getFollowers(int userId);
	
	// DASHBOARD
	@Query("select count(u) from User u")
	Integer getTotalNumberOfUsersRegistered();
	
	@Query("select u1 from Barter b1 left join b1.user u1 group by u1 having count(u1) >= all(select count(u2) from Barter b2 left join b2.user u2 group by u2)")
	Collection<User> getUsersWithMoreBarters();
	
	@Query("select u1 from Barter b1 left join b1.user u1 where b1.cancelled IS TRUE group by u1 having count(u1) >= all(select count(u2) from Barter b2 left join b2.user u2 where b2.cancelled IS TRUE group by u2)")
	Collection<User> getUsersWithMoreBartersCancelled();
	
	@Query("select count(c) from Complaint c right join c.user u group by u")
	Collection<Long> getUsersWhoHaveCreatedMoreComplaintsThanTheAverage();
}