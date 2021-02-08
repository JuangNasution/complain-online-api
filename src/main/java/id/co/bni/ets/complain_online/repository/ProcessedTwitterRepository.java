package id.co.bni.ets.complain_online.repository;

import id.co.bni.ets.complain_online.model.ProcessedTwitter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juang Nasution
 */
@Repository
public interface ProcessedTwitterRepository extends JpaRepository<ProcessedTwitter, String> {

    @Query(value = "select distinct id from processed_twitter", nativeQuery = true)
    List<String> findProcessedTwitter();
}
