package com.alsvietnam.models.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Duc_Huy
 * Date: 9/8/2022
 * Time: 11:25 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto extends BaseUser {
    
    private String phone;

    private String description;

    private MultipartFile avatar;

}
