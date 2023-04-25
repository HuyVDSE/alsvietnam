package com.alsvietnam.repository;

import com.alsvietnam.entities.HonoredUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:14 PM
 */

public interface HonoredUserRepository extends JpaRepository<HonoredUser, String> {

    List<HonoredUser> findByHonoredTable_IdIn(Collection<String> HonoredTableId);

    void deleteAllByHonoredTableId(String honoredTableId);

}
