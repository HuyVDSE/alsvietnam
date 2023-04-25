package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Team;
import com.alsvietnam.models.dtos.team.CreateTeamDto;
import com.alsvietnam.models.dtos.team.TeamDto;
import com.alsvietnam.models.dtos.team.UpdateTeamMember;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.EnumConst;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 11:34 PM
 */

@RestController
@RequestMapping(Constants.TEAM_SERVICE)
@Tag(name = "Team", description = "Team API")
public class TeamController extends BaseController {

    @Operation(summary = "Xem danh sách Team", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = TeamDto.class))))
    })
    @GetMapping
    public List<TeamDto> getTeams() {
        List<Team> teams = teamService.findAll();
        return teamConverter.toDTOs(teams);
    }

    @Operation(summary = "Tạo Team", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TeamDto.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @PostMapping
    public TeamDto createTeam(@RequestBody @Valid CreateTeamDto teamDto) {
        Team team = teamService.createTeam(teamDto);
        return teamConverter.toDTO(team);
    }

    @Operation(summary = "Cập nhật Team", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TeamDto.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @PutMapping
    public TeamDto updateTeam(@RequestBody TeamDto teamDto) {
        Team team = teamService.updateTeam(teamDto.getId(), teamDto.getName());
        return teamConverter.toDTO(team);
    }

    @Operation(summary = "Xóa Team", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @DeleteMapping("{team_id}")
    public ObjectResponseWrapper deleteRole(@PathVariable(value = "team_id") String teamId) {
        teamService.deleteTeam(teamId);
        return ObjectResponseWrapper.builder().status(1)
                .data("team_id " + teamId + " deleted")
                .build();
    }

    @Operation(summary = "Cập nhật thành viên của Team", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @PutMapping("{team_id}/members")
    public ObjectResponseWrapper updateTeam(@PathVariable("team_id") String teamId,
                                            @RequestBody @Valid UpdateTeamMember model) {
        teamService.updateTeamMember(teamId, model.getUserId(), model.getIsAdd());
        return ObjectResponseWrapper.builder().status(1)
                .data("update team member success")
                .build();
    }

    @Operation(summary = "Cập nhật leader của Team", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    @PutMapping("{team_id}/leader/{leader_id}")
    public ObjectResponseWrapper setTeamLeader(@PathVariable("team_id") String teamId,
                                               @PathVariable("leader_id") String leaderId) {
        teamService.setTeamLeader(teamId, leaderId);
        return ObjectResponseWrapper.builder().status(1)
                .data("Set team leader success")
                .build();
    }
}
