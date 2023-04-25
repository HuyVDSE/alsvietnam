package com.alsvietnam.repository;

import com.alsvietnam.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Duc_Huy
 * Date: 9/15/2022
 * Time: 11:16 PM
 */

public interface TaskRepository extends JpaRepository<Task, String>, TaskRepositoryCustom {

    void deleteByArticle_Id(String articleId);

    <T> List<T> findBy(Class<T> tClass);
}
