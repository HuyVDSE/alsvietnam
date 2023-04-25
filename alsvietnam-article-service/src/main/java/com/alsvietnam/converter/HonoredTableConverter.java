package com.alsvietnam.converter;

import com.alsvietnam.entities.HonoredTable;
import com.alsvietnam.entities.HonoredUser;
import com.alsvietnam.models.dtos.honoredVolunteer.CreateHonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.HonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.HonoredUserDto;
import com.alsvietnam.models.dtos.honoredVolunteer.UpdateHonoredTableDto;
import com.alsvietnam.models.dtos.user.UserDto;
import com.alsvietnam.models.search.ParameterSearchHonoredTable;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:40 PM
 */

@Component
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
public class HonoredTableConverter extends BaseConverter {

    private final UserConverter userConverter;

    public List<HonoredTableDto> toDTO(List<HonoredTable> honoredTables, ParameterSearchHonoredTable searchParam) {
        return honoredTables.stream()
                .map(honoredTable -> toDTO(honoredTable, searchParam))
                .collect(Collectors.toList());
    }

    public HonoredTableDto toDTO(HonoredTable honoredTable, ParameterSearchHonoredTable searchParam) {
        HonoredTableDto dto = modelMapper.map(honoredTable, HonoredTableDto.class);
        if (honoredTable.getHonoredUsers() != null) {
            List<HonoredUserDto> users = honoredTable.getHonoredUsers().stream()
                    .map(honoredUser -> toDTO(honoredUser, searchParam))
                    .collect(Collectors.toList());
            List<HonoredUserDto> goldUsers = users.stream()
                    .filter(honoredUserDto -> honoredUserDto.getMedal().equals("GOLD"))
                    .collect(Collectors.toList());
            List<HonoredUserDto> silverUsers = users.stream()
                    .filter(honoredUserDto -> honoredUserDto.getMedal().equals("SILVER"))
                    .collect(Collectors.toList());
            List<HonoredUserDto> bronzeUsers = users.stream()
                    .filter(honoredUserDto -> honoredUserDto.getMedal().equals("BRONZE"))
                    .collect(Collectors.toList());
            List<HonoredUserDto> sortHonoredUser = new ArrayList<>();
            sortHonoredUser.addAll(goldUsers);
            sortHonoredUser.addAll(silverUsers);
            sortHonoredUser.addAll(bronzeUsers);
            dto.setHonoredUsers(sortHonoredUser);
        }
        return dto;
    }

    public HonoredUserDto toDTO(HonoredUser honoredUser, ParameterSearchHonoredTable searchParam) {
        HonoredUserDto dto = modelMapper.map(honoredUser, HonoredUserDto.class);
        dto.setUserId(honoredUser.getUser().getId());
        if (searchParam.isBuildUser()) {
            UserDto userDto = userConverter.toUserDTO(honoredUser.getUser(), false, false);
            dto.setUserDto(userDto);
        }
        return dto;
    }

    public HonoredTable fromCreateDTO(CreateHonoredTableDto honoredTableDto) {
        return HonoredTable.builder()
                .title(honoredTableDto.getTitle())
                .quarter(honoredTableDto.getQuarter())
                .year(honoredTableDto.getYear())
                .active(honoredTableDto.isActive())
                .deleted(false)
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .build();
    }

    public HonoredUser fromCreateDTO(HonoredUserDto honoredUserDto) {
        HonoredUser honoredUser = new HonoredUser();
        honoredUser.setDescription(honoredUserDto.getDescription());
        honoredUser.setRole(honoredUserDto.getRole());
        honoredUser.setMedal(honoredUserDto.getMedal());
        honoredUser.setCreatedAt(new Date());
        honoredUser.setCreatedBy(userService.getUsernameLogin());
        return honoredUser;
    }

    public Set<HonoredUser> fromCreateDTO(List<HonoredUserDto> honoredUserDTOs) {
        if (honoredUserDTOs.isNullOrEmpty()) {
            return Collections.emptySet();
        }
        return honoredUserDTOs.stream().map(this::fromCreateDTO).collect(Collectors.toSet());
    }

    public void fromUpdateDTO(HonoredTable honoredTable, UpdateHonoredTableDto honoredTableDto) {
        honoredTable.setTitle(honoredTableDto.getTitle());
        honoredTable.setQuarter(honoredTableDto.getQuarter());
        honoredTable.setYear(honoredTableDto.getYear());
        honoredTable.setActive(honoredTableDto.isActive());
        honoredTable.setDeleted(honoredTableDto.isDeleted());
        honoredTable.setUpdatedBy(userService.getUsernameLogin());
        honoredTable.setUpdatedAt(new Date());
    }
}
