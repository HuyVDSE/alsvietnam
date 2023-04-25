package com.alsvietnam.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateGetInvolveUserDto extends BaseUser {

    @Length(min = 6, message = "Password minimum is 6")
    @NotEmpty(message = "Password is required")
    private String password;

    private String description;

    @NotEmpty(message = "Role is required")
    private String role;

    private String phone;
}
