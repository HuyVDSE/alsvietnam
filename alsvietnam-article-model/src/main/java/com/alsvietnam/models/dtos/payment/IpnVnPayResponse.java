package com.alsvietnam.models.dtos.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Duc_Huy
 * Date: 8/24/2022
 * Time: 12:23 PM
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IpnVnPayResponse {

    @JsonProperty("RspCode")
    private String rspCode;

    @JsonProperty("Message")
    private String message;

}
