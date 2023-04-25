package com.alsvietnam.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/5/2022
 * Time: 9:45 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateUserDto extends BaseUser {

    @Length(min = 6, message = "Password minimum is 6")
    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Role is required")
    private String role;

    @NotBlank(message = "phone is required")
    private String phone;

    private List<String> teams;

    private MultipartFile avatar;
}
