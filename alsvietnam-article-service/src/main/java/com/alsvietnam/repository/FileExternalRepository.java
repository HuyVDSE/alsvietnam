package com.alsvietnam.repository;

import com.alsvietnam.entities.FileExternal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Duc_Huy
 * Date: 11/27/2022
 * Time: 9:45 PM
 */

public interface FileExternalRepository extends JpaRepository<FileExternal, String> {

    FileExternal findByUrl(String url);
}
