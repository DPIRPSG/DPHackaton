package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Finances;

@Repository
public interface FinancesRepository extends JpaRepository<Finances, Integer> {

}
