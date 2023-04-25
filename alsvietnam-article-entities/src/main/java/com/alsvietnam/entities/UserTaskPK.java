package com.alsvietnam.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Duc_Huy
 * Date: 9/19/2022
 * Time: 9:08 PM
 */

@Embeddable
@Data
public class UserTaskPK implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "task_id")
    private String taskId;

}
