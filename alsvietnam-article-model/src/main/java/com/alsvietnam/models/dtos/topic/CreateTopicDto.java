package com.alsvietnam.models.dtos.topic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTopicDto {

    @NotBlank(message = "title vietnamese is required")
    private String titleVietnamese;

    @NotBlank(message = "title english is required")
    private String titleEnglish;

    private String topicParentId;

    private String description;

    private Boolean active = true;

}
