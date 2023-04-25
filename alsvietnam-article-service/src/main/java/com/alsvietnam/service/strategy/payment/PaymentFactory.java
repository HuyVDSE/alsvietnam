package com.alsvietnam.service.strategy.payment;

import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.utils.EnumConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Duc_Huy
 * Date: 8/24/2022
 * Time: 8:49 AM
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentFactory {

    private final MomoPaymentStrategy momoPaymentStrategy;
    private final VnPayPaymentStrategy vnPayPaymentStrategy;

    public PaymentStrategy createStrategy(String paymentGateway) {
        log.info("Active payment gateway '{}'", paymentGateway);

        switch (EnumConst.PaymentGatewayEnum.valueOf(paymentGateway.toUpperCase())) {
            case VNPAY:
                return this.vnPayPaymentStrategy;
            case MOMO:
                return this.momoPaymentStrategy;
            default:
                throw new ServiceException("payment gateway " + paymentGateway + " not supported yet");
        }
    }
}
