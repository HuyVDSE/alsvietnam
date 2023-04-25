package com.alsvietnam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 11/19/2022
 * Time: 11:16 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordModel {

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "verify Code is required")
    private String verifyCode;

    @NotBlank(message = "password is required")
    @Length(min = 6, max = 50, message = "password length is from 6 to 50 character")
    private String password;

    @NotBlank(message = "confirm password is required")
    private String confirmPassword;

}
