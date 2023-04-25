package com.alsvietnam.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Duc_Huy
 * Date: 9/6/2022
 * Time: 11:33 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BaseUser {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "Incorrect email format")
    private String email;

    @NotBlank(message = "firstName is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "lastName is required")
    private String lastName;

    private String address;

    private String major;

    @NotNull(message = "status is required (status of account ~ verified)")
    private Boolean status;

    @NotNull(message = "approve status is required")
    private Boolean approveStatus;

    private String socialLink;

}
