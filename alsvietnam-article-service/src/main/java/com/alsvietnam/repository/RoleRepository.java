package com.alsvietnam.repository;

import com.alsvietnam.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Duc_Huy
 * Date: 6/25/2022
 * Time: 5:51 PM
 */

public interface RoleRepository extends JpaRepository<Role, String>, RoleRepositoryCustom {
    Optional<Role> findByName(String name);

    List<Role> findByNameContainsIgnoreCase(String name);

    List<Role> findByNameIn(Collection<String> names);
}
