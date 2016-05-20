package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

//	@Query("select c from Comment c where c.commentedEntity.id = ?1 and c.deleted = false")
//	Collection<Comment> findAllByCommentedEntityId(int commentedEntityId);
	
}
