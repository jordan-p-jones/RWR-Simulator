package rwr.sim;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface to provide data access methods for RwrContact.
 * Custom methods not pre-implemented by JpaRepository will go here.
 */
public interface RwrContactRepository extends JpaRepository<RwrContact, Long>
{

}
