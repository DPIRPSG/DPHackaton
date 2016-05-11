package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Autoreply;

@Repository
public interface AutoreplyRepository extends JpaRepository<Autoreply, Integer> {

	@Query("select a from Autoreply a join a.actor u where u.id = ?1")
	Collection<Autoreply> findByActorId(int actorId);

}
