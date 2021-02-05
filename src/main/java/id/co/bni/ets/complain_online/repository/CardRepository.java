package id.co.bni.ets.complain_online.repository;

import id.co.bni.ets.complain_online.model.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juang Nasution
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    @Query(value = "select u "
            + "from #{#entityName} u "
            + "where "
            + "     u.customerId = ?1 "
            + "     and u.cardNumber like %?2%")
    List<Card> findByCustomerId(int customerId, String cardNumber);

    Card findByCardNumber(String card);
}
