package com.alsvietnam.repository;

import com.alsvietnam.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 9:02 PM
 */

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {

    List<PaymentMethod> findAllByStatus(boolean status);

    Optional<PaymentMethod> findByName(String name);
}
