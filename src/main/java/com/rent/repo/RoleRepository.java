package com.rent.repo;

import org.springframework.data.repository.CrudRepository;

import com.rent.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByRole(String role);

}
