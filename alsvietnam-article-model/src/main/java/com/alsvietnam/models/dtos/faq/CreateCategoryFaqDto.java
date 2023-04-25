package com.alsvietnam.models.dtos.faq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 6:36 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreateCategoryFaqDto {

    private String vnName;

    private String enName;

}
