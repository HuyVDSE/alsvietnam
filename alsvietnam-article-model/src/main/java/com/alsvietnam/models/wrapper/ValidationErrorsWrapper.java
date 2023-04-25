package com.alsvietnam.models.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Duc_Huy
 * Date: 9/19/2022
 * Time: 4:37 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ValidationErrorsWrapper {

    private int status;

    private List<String> message;

}
