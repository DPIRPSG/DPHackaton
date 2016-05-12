package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Participates;

@Repository
public interface ParticipatesRepository extends JpaRepository<Participates, Integer> {

}
