package com.alsvietnam.service.impl;

import com.alsvietnam.entities.PaymentMethod;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.payment.CreatePaymentMethodDto;
import com.alsvietnam.models.dtos.payment.UpdatePaymentMethodDto;
import com.alsvietnam.service.PaymentMethodService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 9:29 PM
 */

@Service
@ExtensionMethod(Extensions.class)
@Slf4j
public class PaymentMethodServiceImpl extends BaseService implements PaymentMethodService {

    @Override
    public List<PaymentMethod> getAll(Boolean status) {
        log.info("GET payment methods");
        if (status == null) {
            return paymentMethodRepository.findAll();
        }
        return paymentMethodRepository.findAllByStatus(status);
    }

    @Override
    public PaymentMethod getById(String id) {
        log.info("GET payment method by Id: {}", id);
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isEmpty()) {
            throw new ServiceException("payment method " + id + " not found");
        }
        return paymentMethod.get();
    }

    @Override
    public PaymentMethod create(CreatePaymentMethodDto paymentMethodDto) {
        log.info("Create payment method");
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findByName(paymentMethodDto.getName());
        if (paymentMethod.isPresent()) {
            throw new ServiceException("Payment method " + paymentMethodDto.getName() + " already existed");
        }
        PaymentMethod entity = PaymentMethod.builder()
                .name(paymentMethodDto.getName())
                .description(paymentMethodDto.getDescription())
                .status(true)
                .createdAt(new Date())
                .createdBy("admin") // set current user login
                .build();
        return paymentMethodRepository.save(entity);
    }

    @Override
    public PaymentMethod update(UpdatePaymentMethodDto paymentMethodDto) {
        log.info("Update payment method {}", paymentMethodDto.getId());
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(paymentMethodDto.getId());
        if (paymentMethod.isEmpty()) {
            throw new ServiceException("payment method " + paymentMethodDto.getId() + " not found");
        }
        PaymentMethod entity = paymentMethod.get();
        entity.setName(paymentMethodDto.getName());
        entity.setDescription(paymentMethodDto.getDescription());
        entity.setStatus(paymentMethodDto.isStatus());
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy("admin"); // set current user login
        return paymentMethodRepository.save(entity);
    }

    @Override
    public void delete(String id, Boolean hardDelete) {
        log.info("Delete payment method {}", id);
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isEmpty()) {
            throw new ServiceException("payment method " + id + " not found");
        }
        PaymentMethod entity = paymentMethod.get();
        if (hardDelete != null && hardDelete) {
            paymentMethodRepository.delete(entity);
        } else {
            entity.setStatus(false);
            paymentMethodRepository.save(entity);
        }
    }
}
