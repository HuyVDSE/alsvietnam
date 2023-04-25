package com.alsvietnam.repository;

import com.alsvietnam.entities.UserTask;
import com.alsvietnam.entities.UserTaskPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/19/2022
 * Time: 9:13 PM
 */

public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskPK> {

    List<UserTask> findByTask_IdIn(Collection<String> taskIds);
}
