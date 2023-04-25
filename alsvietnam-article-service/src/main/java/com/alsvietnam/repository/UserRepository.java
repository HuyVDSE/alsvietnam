package com.alsvietnam.repository;

import com.alsvietnam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole_Name(String roleName);

    List<User> findByTeams_Name(String teamName);

    List<User> findByTeams_IdIn(Collection<String> teamIds);

    Optional<User> findByIdAndRole_Name(String userId, String roleName);

    List<User> findByIdIn(Collection<String> ids);
}
