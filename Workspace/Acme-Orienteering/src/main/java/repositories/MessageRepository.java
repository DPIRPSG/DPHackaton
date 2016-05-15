package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageEntity;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {

	@Query("select m from MessageEntity m join m.folders f where f.id = ?1")
	Collection<MessageEntity> findAllByFolderId(int folderId);
}
