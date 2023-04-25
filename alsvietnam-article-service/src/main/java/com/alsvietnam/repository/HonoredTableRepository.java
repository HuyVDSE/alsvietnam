package com.alsvietnam.repository;

import com.alsvietnam.entities.HonoredTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:14 PM
 */

public interface HonoredTableRepository extends JpaRepository<HonoredTable, String>, HonoredTableRepositoryCustom {

    List<HonoredTable> findByIdNotAndActive(String honoredTableId, Boolean active);
}
