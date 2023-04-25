package com.alsvietnam.models.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 10:01 PM
 */

@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class StringResponseWrapper extends ResponseWrapper<String> {
}
