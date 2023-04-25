package com.alsvietnam.models.dtos.story;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateStoryDto extends CreateStoryDto {

    @NotBlank(message = "id is required")
    private String id;

    private Boolean deleted;

    private String articleId;
}
