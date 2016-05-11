package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Barter;

@Repository
public interface BarterRepository extends JpaRepository<Barter, Integer> {

	@Query("select b from Barter b where b.cancelled = false order by b.registerMoment desc")
	Collection<Barter> findAllNotCancelled();

	@Query("select b from Barter b where (b.title like concat('%',?1,'%') or b.offered.name like concat('%',?1,'%') or b.offered.description like concat('%',?1,'%') or b.requested.name like concat('%',?1,'%') or b.requested.description like concat('%',?1,'%')) and b.cancelled = false")
	Collection<Barter> findBySingleKeywordNotCancelled(String keyword);
	
	@Query("select b from Barter b where (b.title like concat('%',?1,'%') or b.offered.name like concat('%',?1,'%') or b.offered.description like concat('%',?1,'%') or b.requested.name like concat('%',?1,'%') or b.requested.description like concat('%',?1,'%'))")
	Collection<Barter> findBySingleKeyword(String keyword);
	
	@Query("select b from Barter b join b.user u where u.id = ?1 and b.cancelled = false order by b.registerMoment desc")
	Collection<Barter> findByUserIdNotCancelled(int userId);
	
	@Query("select b from Barter b join b.user u where u.id = ?1 and b.cancelled = false group by b having b not in (select m.creatorBarter from Match m where m.cancelled = false) and b not in (select m.receiverBarter from Match m where m.cancelled = false)")
	Collection<Barter> findByUserIdNotCancelledNotInMatchNotCancelled(int userId);
	
	@Query("select b from Barter b join b.user u where u.id != ?1 and b.cancelled = false group by b having b not in (select m.creatorBarter from Match m where m.cancelled = false) and b not in (select m.receiverBarter from Match m where m.cancelled = false)")
	Collection<Barter> findAllOfOtherUsersByUserIdNotCancelledNotInMatchNotCancelled(int userId);

	@Query("select b from User a join a.followed u, Barter b where a.id=?1 and b.user.id = u.id order by b.registerMoment desc")
	Collection<Barter> findAllByFollowedUser(int userId);

	@Query("select b from Barter b join b.relatedBarter r where r.id=?1")
	Collection<Barter> getOtherRelatedBartersById(int barterId);
	
	// DASHBOARD
	@Query("select count(b) from Barter b")
	Integer getTotalNumberOfBarterRegistered();
	
	@Query("select count(b) from Barter b where b.cancelled IS TRUE")
	Integer getTotalNumberOfBarterCancelled();

	@Query("select b from Barter b where b.offered.id = ?1 or b.requested.id = ?1")
	Barter findOneByItemId(int itemId);
	
	@Query("select count(c) from Complaint c right join c.barter b group by b")
	Collection<Long> getCountOfComplaintsPerBarter();
}
