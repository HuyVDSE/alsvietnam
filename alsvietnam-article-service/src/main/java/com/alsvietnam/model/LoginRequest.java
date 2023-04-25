package com.alsvietnam.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * Duc_Huy
 * Date: 9/4/2022
 * Time: 4:18 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "username không được để trống")
    private String username;

    @NotEmpty(message = "password không được để trống")
    private String password;
}
