package com.alsvietnam.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 9/8/2022
 * Time: 11:51 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPassword {

    @NotBlank(message = "Please enter username")
    private String username;

    @NotBlank(message = "Please enter old password")
    private String oldPassword;

    @NotBlank(message = "Please enter new password")
    private String newPassword;

    @NotBlank(message = "Please enter confirm password")
    private String confirmPassword;

}
