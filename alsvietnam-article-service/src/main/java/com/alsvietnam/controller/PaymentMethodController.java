package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.PaymentMethod;
import com.alsvietnam.models.dtos.payment.CreatePaymentMethodDto;
import com.alsvietnam.models.dtos.payment.UpdatePaymentMethodDto;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 9:58 PM
 */

@RestController
@RequestMapping(Constants.PAYMENT_METHOD_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "Payment Method", description = "Payment Method API")
public class PaymentMethodController extends BaseController {

    @GetMapping
    @Operation(summary = "Danh sách phương thức thanh toán", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = PaymentMethod.class))))
    })
    public List<PaymentMethod> getPaymentMethods(@Parameter(description = "Allowed values: true | false")
                                                 @RequestParam(value = "status", required = false) Boolean status) {
        return paymentMethodService.getAll(status);
    }

    @GetMapping("{id}")
    @Operation(summary = "Lấy phương thức thanh toán theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaymentMethod.class)))
    })
    public PaymentMethod getPaymentMethod(@PathVariable("id") String id) {
        return paymentMethodService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Tạo phương thức thanh toán", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaymentMethod.class)))
    })
    public PaymentMethod createPaymentMethod(@RequestBody @Valid CreatePaymentMethodDto paymentMethodDto) {
        return paymentMethodService.create(paymentMethodDto);
    }

    @PutMapping
    @Operation(summary = "Cập nhật phương thức thanh toán", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = PaymentMethod.class)))
    })
    public PaymentMethod updatePaymentMethod(@RequestBody @Valid UpdatePaymentMethodDto paymentMethodDto) {
        return paymentMethodService.update(paymentMethodDto);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Xoá phương thức thanh toán", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    public void deletePaymentMethod(@PathVariable("id") String id,
                                    @RequestParam(value = "hard_delete", required = false) Boolean hardDelete) {
        paymentMethodService.delete(id, hardDelete);
    }
}
