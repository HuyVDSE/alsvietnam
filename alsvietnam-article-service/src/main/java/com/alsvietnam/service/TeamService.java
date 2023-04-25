package com.alsvietnam.service;

import com.alsvietnam.entities.Team;
import com.alsvietnam.models.dtos.team.CreateTeamDto;

import java.util.List;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 10:58 PM
 */
public interface TeamService {

    List<Team> findAll();

    Team createTeam(CreateTeamDto teamDto);

    Team updateTeam(String id, String newName);

    void deleteTeam(String id);

    void updateTeamMember(String userId, String teamId, boolean isAdd);

    void setTeamLeader(String teamId, String leaderId);
}
