package com.alsvietnam.models.profiles;

import com.alsvietnam.models.dtos.task.BaseTaskDto;
import com.alsvietnam.models.dtos.team.TeamDto;
import com.alsvietnam.models.dtos.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 10:01 AM
 */

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TaskProfile extends BaseTaskDto {

    private String id;

    private TeamDto team;

    private UserDto manager;

    private List<UserDto> users;

    private String articleId;

    private String actualStartDate;

    private String actualEndDate;

    private ArticleProfile articleProfile;

}
