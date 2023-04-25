package com.alsvietnam.models.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ReportCommentDto {

    @NotBlank(message = "Comment id is required")
    private String id;

    @NotBlank(message = "Flag content is required")
    private String flagContent;
}
