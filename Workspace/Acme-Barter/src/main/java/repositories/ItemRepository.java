package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	@Query("select b.offered from Barter b where b.user.id = ?1")
	Collection<Item> findAllOfferedByUser(int userId);
	
	@Query("select b.requested from Barter b where b.user.id = ?1")
	Collection<Item> findAllRequestedByUser(int userId);
	
}
