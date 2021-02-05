package id.co.bni.ets.complain_online.repository;

import id.co.bni.ets.complain_online.model.Complain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juang Nasution
 */
@Repository
public interface ComplainRepository extends PagingAndSortingRepository<Complain, String> {

    @Query(value = "select u "
            + "from #{#entityName} u "
            + "where "
            + "     u.customerId = ?1 "
            + "     and u.subject like %?2% ")
    Page<Complain> findAllByCustomerId(int customerId, String searchTerm, Pageable pageable);

    @Query(value = "select u "
            + "from #{#entityName} u "
            + "where "
            + "     u.subject like %?1% "
            + "     and u.status = 0 "
            + "     and u.category like %?2% ")
    Page<Complain> findAllAtm(String searchTerm, String category,
            Pageable pageable);
}
