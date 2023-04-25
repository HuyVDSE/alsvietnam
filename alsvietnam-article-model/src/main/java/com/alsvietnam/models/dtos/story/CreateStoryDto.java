package com.alsvietnam.models.dtos.story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:08 PM
 */

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateStoryDto {

    @NotBlank(message = "title is required")
    private String title;

    private String link;

    @NotBlank(message = "content is required")
    private String content;

    @NotBlank(message = "UserId is required")
    private String userId;

}
