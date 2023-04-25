package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Team;
import com.alsvietnam.entities.User;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.team.CreateTeamDto;
import com.alsvietnam.service.TeamService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 10:59 PM
 */

@Service
@ExtensionMethod(Extensions.class)
@Slf4j
public class TeamServiceImpl extends BaseService implements TeamService {

    @Override
    public List<Team> findAll() {
        log.info("fetch all teams");
        return teamRepository.findAll();
    }

    @Override
    public Team createTeam(CreateTeamDto teamDto) {
        String name = teamDto.getName();

        log.info("create new team {}", name);
        teamRepository.findByName(name).ifPresent(team -> {
            throw new ServiceException("Name {" + name + "} already exists");
        });
        Team team = Team.builder()
                .name(name)
                .createdAt(new Date())
                .createdBy(getCurrentUsername())
                .build();
        if (!teamDto.getLeaderId().isBlankOrNull()) {
            User leader = userRepository.getById(teamDto.getLeaderId());
            team.setLeader(leader);
        }

        teamRepository.save(team);
        logDataService.create(team.getId(), EnumConst.LogTypeEnum.TEAM.name(), "Create team " + name);
        return team;
    }

    @Override
    public Team updateTeam(String id, String newName) {
        log.info("update team {} to {}", id, newName);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Team " + id + " not found"));
        team.setName(newName);
        logDataService.create(team.getId(), EnumConst.LogTypeEnum.TEAM.name(), "update team " + id + " to " + newName);
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(String id) {
        log.info("delete team {}", id);
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Team " + id + " not found"));
        List<User> usersByTeam = userRepository.findByTeams_Name(team.getName());
        if (!usersByTeam.isEmpty()) {
            throw new ServiceException("Can't delete, members of team {" + team.getName() + "} is not empty");
        }
        logDataService.create(team.getId(), EnumConst.LogTypeEnum.TEAM.name(), "delete team " + id);
        teamRepository.delete(team);
    }

    @Override
    public void updateTeamMember(String teamId, String userId, boolean isAdd) {
        log.info("Adding user {} to team {}", userId, teamId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("User " + userId + " not found"));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ServiceException("Team " + teamId + " not found"));
        if (isAdd) {
            team.addUser(user);
        } else {
            team.removeUser(user);
        }
        teamRepository.save(team);
    }

    @Override
    public void setTeamLeader(String teamId, String leaderId) {
        log.info("Set leader {} for team {}", leaderId, teamId);
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ServiceException("Team " + teamId + " not found"));
        if (team.getLeader() != null && team.getLeader().getId().equals(leaderId)) {
            log.info("leader " + leaderId + " already exist in this team");
            return;
        }
        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new ServiceException("User " + leaderId + " not found"));
        if (leader.getTeams() != null && !leader.getTeams().contains(team)) {
            throw new ServiceException("this user is not a member of this team");
        }
        team.setLeader(leader);
        teamRepository.save(team);
    }
}
