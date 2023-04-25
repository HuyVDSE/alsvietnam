package com.alsvietnam.models.dtos.payment;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 9:33 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdatePaymentMethodDto extends CreatePaymentMethodDto {

    @NotBlank(message = "Id phương thức thanh toán không được để trống")
    private String id;

    private boolean status;

}
