package com.alsvietnam.converter;

import com.alsvietnam.entities.Team;
import com.alsvietnam.models.dtos.team.TeamDto;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 11:49 AM
 */

@Component
public class TeamConverter {

    public List<TeamDto> toDTOs(Collection<Team> teams) {
        return teams.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public TeamDto toDTO(Team team) {
        TeamDto dto = new TeamDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setLeaderId(team.getLeader() != null ? team.getLeader().getId() : null);
        return dto;
    }
}
