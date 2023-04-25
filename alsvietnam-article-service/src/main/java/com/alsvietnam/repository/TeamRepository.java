package com.alsvietnam.repository;

import com.alsvietnam.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 9:39 PM
 */

public interface TeamRepository extends JpaRepository<Team, String> {

    Optional<Team> findByName(String name);

    Set<Team> findByIdIn(Collection<String> teamIds);

    List<Team> findByUsers_Id(String userId);
}
