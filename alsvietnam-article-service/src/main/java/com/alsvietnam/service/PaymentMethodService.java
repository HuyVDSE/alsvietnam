package com.alsvietnam.service;

import com.alsvietnam.entities.PaymentMethod;
import com.alsvietnam.models.dtos.payment.CreatePaymentMethodDto;
import com.alsvietnam.models.dtos.payment.UpdatePaymentMethodDto;

import java.util.List;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 9:03 PM
 */
public interface PaymentMethodService {

    List<PaymentMethod> getAll(Boolean status);

    PaymentMethod getById(String id);

    PaymentMethod create(CreatePaymentMethodDto paymentMethodDto);

    PaymentMethod update(UpdatePaymentMethodDto paymentMethodDto);

    void delete(String id, Boolean hardDelete);
}
