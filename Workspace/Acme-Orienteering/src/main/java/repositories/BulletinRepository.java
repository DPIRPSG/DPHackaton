package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Bulletin;

@Repository
public interface BulletinRepository extends JpaRepository<Bulletin, Integer> {

}
