package rwr.sim;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to provide data access methods for SignalEventRepository.
 * Custom methods not pre-implemented by JpaRepository will go here.
 */
public interface SignalEventRepository extends JpaRepository<SignalEvent, Long>
{

}
