package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.AttributeDescription;

@Repository
public interface AttributeDescriptionRepository extends JpaRepository<AttributeDescription, Integer> {

	@Query("select a from AttributeDescription a where a.item.id = ?1")
	Collection<AttributeDescription> findAllByItemId(int itemId);
	
}
