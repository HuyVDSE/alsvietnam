package com.alsvietnam.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Duc_Huy
 * Date: 9/3/2022
 * Time: 11:46 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRole {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "roleId is required")
    private String roleId;

}
