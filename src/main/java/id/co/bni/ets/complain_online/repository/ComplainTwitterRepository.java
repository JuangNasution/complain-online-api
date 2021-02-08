package id.co.bni.ets.complain_online.repository;

import id.co.bni.ets.complain_online.model.ComplainTwitter;
import java.sql.Date;
import java.util.List;
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
public interface ComplainTwitterRepository extends PagingAndSortingRepository<ComplainTwitter, String> {

    @Query(value = "select u "
            + "from #{#entityName} u "
            + "where "
            + "     u.subject like %?1% "
            + "     and u.status = 0 "
            + "     and u.category like %?2% ")
    Page<ComplainTwitter> findAllTwitter(String searchTerm, String category,
            Pageable pageable);

    @Query(value = "select u "
            + "from #{#entityName} u "
            + "where "
            + "     convert(varchar, u.createdDate, 110) between ?1 and ?2 "
            + "     and u.category like %?3% ")
    Page<ComplainTwitter> findByLoadDateBetween(Date from, Date to, String category, Pageable pageable);

    @Query(value = "select u "
            + "from #{#entityName} u "
            + "where "
            + "     convert(varchar, u.createdDate, 110) between ?1 and ?2 "
            + "     and u.category like %?3% ")
    List<ComplainTwitter> exportTwitter(Date from, Date to, String category);
}
