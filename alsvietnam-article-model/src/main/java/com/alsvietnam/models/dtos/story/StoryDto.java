package com.alsvietnam.models.dtos.story;

import com.alsvietnam.models.profiles.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:10 PM
 */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoryDto extends CreateStoryDto {

    @NotBlank(message = "id is required")
    private String id;

    private Boolean deleted;

    private String articleId;

    private UserProfile userProfile;

    private String createdBy;

    private Date createdAt;
}
