package id.co.bni.ets.complain_online.repository;

import id.co.bni.ets.complain_online.model.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Juang Nasution
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

    Customer findByEmail(String email);
}
