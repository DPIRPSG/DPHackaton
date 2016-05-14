package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {

}
