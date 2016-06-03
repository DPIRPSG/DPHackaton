package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Punishment;

@Repository
public interface PunishmentRepository extends JpaRepository<Punishment, Integer> {

	@Query("select p from Punishment p where p.club.id = ?1")
	Collection<Punishment> findAllByClubId(Integer clubId);

}
