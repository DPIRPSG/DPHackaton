package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.League;

@Repository
public interface LeagueRepository extends JpaRepository<League, Integer> {

}
