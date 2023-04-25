package com.alsvietnam.models.dtos.payment;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 9:31 PM
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CreatePaymentMethodDto {

    @NotBlank(message = "Tên phương thức thanh toán không được để trống")
    private String name;

    private String description;

}
