package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finances;

@Repository
public interface FinancesRepository extends JpaRepository<Finances, Integer> {

	@Query("select f from Finances f where f.sponsor.id = ?1 and f.league.id = ?2")
	Collection<Finances> findAllBySponsorIdAndLeagueId(int sponsorId, int leagueId);

	@Query("select f from Finances f where f.sponsor.id = ?1")
	Collection<Finances> findAllBySponsorId(int sponsorId);

	@Query("select f from Finances f where f.league.id = ?1")
	Collection<Finances> findAllByLeagueId(int leagueId);

}
