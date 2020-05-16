package com.rent.repo;
import org.springframework.data.repository.CrudRepository;

import com.rent.entity.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);

	User findByActivation(String code);
        
        @Query(value = "SELECT * FROM users WHERE "
                            + "id = (SELECT user_id FROM users_roles WHERE "
                                        + "role_id = (SELECT id FROM roles WHERE role = 'MASTER'))", nativeQuery = true)
        User findMaster();
        
}