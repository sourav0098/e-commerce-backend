package com.quickpik.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quickpik.entities.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
