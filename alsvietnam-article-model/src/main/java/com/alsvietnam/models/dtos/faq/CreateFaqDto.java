package com.alsvietnam.models.dtos.faq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 6:39 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateFaqDto {

    @NotBlank(message = "vnQuestion is required")
    private String vnQuestion;

    @NotBlank(message = "enQuestion is required")
    private String enQuestion;

    @NotBlank(message = "vnAnswer is required")
    private String vnAnswer;

    @NotBlank(message = "enAnswer is required")
    private String enAnswer;

    @NotBlank(message = "categoryFaqId is required")
    private String categoryFaqId;

    @NotNull(message = "active is required")
    private Boolean active;

}
