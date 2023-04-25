package com.alsvietnam.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Duc_Huy
 * Date: 9/19/2022
 * Time: 9:08 PM
 */

@Entity
@Table(name = "user_task")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTask {

    @EmbeddedId
    private UserTaskPK id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

}
