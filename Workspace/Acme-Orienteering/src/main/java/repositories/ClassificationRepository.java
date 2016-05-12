package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Classification;

@Repository
public interface ClassificationRepository extends JpaRepository<Classification, Integer> {

}
