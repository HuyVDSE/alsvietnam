package com.alsvietnam.repository;

import com.alsvietnam.entities.Story;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:03 PM
 */

public interface StoryRepository extends JpaRepository<Story, String>, StoryRepositoryCustom {

}
