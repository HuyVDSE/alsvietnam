package com.alsvietnam.converter;

import com.alsvietnam.entities.User;
import com.alsvietnam.models.dtos.role.RoleDto;
import com.alsvietnam.models.dtos.user.CreateGetInvolveUserDto;
import com.alsvietnam.models.dtos.user.CreateUserDto;
import com.alsvietnam.models.dtos.user.UpdateUserDto;
import com.alsvietnam.models.dtos.user.UserDto;
import com.alsvietnam.models.profiles.UserProfile;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 10:09 PM
 */

@Component
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
@Slf4j
public class UserConverter extends BaseConverter {

    private final TeamConverter teamConverter;

    public User fromCreateUserDTO(CreateUserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .major(dto.getMajor())
                .socialLink(dto.getSocialLink())
                .status(dto.getStatus())
                .approveStatus(dto.getApproveStatus())
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .build();
    }

    public User fromCreateGetInvolveUserDTO(CreateGetInvolveUserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .major(dto.getMajor())
                .socialLink(dto.getSocialLink())
                .description(dto.getDescription())
                .approveStatus(dto.getApproveStatus())
                .status(dto.getStatus())
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .build();
    }

    public User fromUpdateUserDTO(User user, UpdateUserDto model) {
        user.setEmail(model.getEmail());
        user.setFirstName(model.getFirstName());
        user.setMiddleName(model.getMiddleName());
        user.setLastName(model.getLastName());
        user.setPhone(model.getPhone());
        user.setAddress(model.getAddress());
        user.setMajor(model.getMajor());
        user.setSocialLink(model.getSocialLink());
        user.setDescription(model.getDescription());
        user.setStatus(model.getStatus());
        user.setApproveStatus(model.getApproveStatus());
        user.setUpdatedAt(new Date());
        user.setUpdatedBy(userService.getUsernameLogin());
        return user;
    }

    public List<UserDto> toUserDTO(List<User> users, ParameterSearchUser parameterSearchUser) {
        return users.stream().map(user -> toUserDTO(user, parameterSearchUser)).collect(Collectors.toList());
    }

    public List<UserDto> toUserDTO(Collection<User> users, boolean buildRole, boolean buildTeam) {
        return users.stream().map(user -> toUserDTO(user, buildRole, buildTeam)).collect(Collectors.toList());
    }

    public UserDto toUserDTO(User user, ParameterSearchUser parameterSearchUser) {
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .major(user.getMajor())
                .status(user.getStatus())
                .approveStatus(user.getApproveStatus())
                .description(user.getDescription())
                .socialLink(user.getSocialLink())
                .build();
        if (!Extensions.isBlankOrNull(user.getAvatar())) {
            try {
                JSONObject avatar = new JSONObject(user.getAvatar());
                dto.setAvatar(avatar.getString("url"));
            } catch (Exception e) {
                log.info("Error when parse user avatar json: " + user.getUsername());
            }
        }
        if (parameterSearchUser.isBuildRole()) {
            dto.setRole(modelMapper.map(user.getRole(), RoleDto.class));
        }
        if (parameterSearchUser.isBuildTeam()) {
            dto.setTeams(teamConverter.toDTOs(user.getTeams()));
        }
        return dto;
    }

    public UserDto toUserDTO(User user, boolean buildRole, boolean buildTeam) {
        ParameterSearchUser parameterSearchUser = new ParameterSearchUser();
        parameterSearchUser.setBuildRole(buildRole);
        parameterSearchUser.setBuildTeam(buildTeam);
        return toUserDTO(user, parameterSearchUser);
    }

    /**
     * Return appropriate field of user for comment
     * @param user entity
     * @return UserProfile
     */
    public UserProfile toProfile(User user) {
        UserProfile dto = UserProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .avatar(user.getAvatar())
                .build();

        if (!Extensions.isBlankOrNull(user.getAvatar())) {
            try {
                JSONObject avatar = new JSONObject(user.getAvatar());
                dto.setAvatar(avatar.getString("url"));
            } catch (Exception e) {
                log.info("Error when parse user avatar json: " + user.getUsername());
            }
        }

        return dto;
    }
}
