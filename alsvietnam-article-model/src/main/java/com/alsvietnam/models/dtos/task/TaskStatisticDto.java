package com.alsvietnam.models.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Duc_Huy
 * Date: 11/24/2022
 * Time: 8:03 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskStatisticDto {

    private Integer taskNew = 0;

    private Integer taskDoing = 0;

    private Integer taskDone = 0;

    private Integer taskDeadline = 0;

    public void addTaskNew(Integer number) {
        taskNew += number;
    }

    public void addTaskDoing(Integer number) {
        taskDoing += number;
    }

    public void addTaskDone(Integer number) {
        taskDone += number;
    }

    public void addTaskDeadline(Integer number) {
        taskDeadline += number;
    }
}
