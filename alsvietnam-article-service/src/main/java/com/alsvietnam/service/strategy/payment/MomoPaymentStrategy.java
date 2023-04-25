package com.alsvietnam.service.strategy.payment;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.wrapper.ResponseWrapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 12:11 AM
 */

@Service
public class MomoPaymentStrategy implements PaymentStrategy {


    @Override
    public String createPaymentLink(Donation donation) {
        throw new ServiceException("Momo payment not supported yet");
    }

    @Override
    public ResponseWrapper<?> handleIPNCall(Map<String, String> params) {
        throw new ServiceException("Handle Momo IPN not supported yet");
    }
}
