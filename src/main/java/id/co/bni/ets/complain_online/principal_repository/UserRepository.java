package id.co.bni.ets.complain_online.principal_repository;

import id.co.bni.ets.lib.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //empty interface
}
