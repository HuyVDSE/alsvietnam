package com.alsvietnam.service.strategy.payment;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.models.wrapper.ResponseWrapper;

import java.util.Map;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 12:04 AM
 */

public interface PaymentStrategy {

    String createPaymentLink(Donation donation);

    ResponseWrapper<?> handleIPNCall(Map<String, String> params);

}
