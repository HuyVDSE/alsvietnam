package com.alsvietnam.repository;

import com.alsvietnam.entities.TopOrganizationSupport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopOrganizationSupportRepository extends JpaRepository<TopOrganizationSupport, String>, TopOrganizationSupportRepositoryCustom{
}
