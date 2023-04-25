package com.alsvietnam.models.dtos.faq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * A DTO for the {@link com.alsvietnam.entities.CategoryFaq} entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CategoryFaqDto extends CreateCategoryFaqDto implements Serializable {

    private String id;

}