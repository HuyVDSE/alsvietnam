package com.alsvietnam.models.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Duc_Huy
 * Date: 6/9/2022
 * Time: 12:55 AM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ResponseWrapper<T> {

    protected int status;

    protected String message;

    protected T data;

}
