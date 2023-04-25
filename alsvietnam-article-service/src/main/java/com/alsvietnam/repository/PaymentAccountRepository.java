package com.alsvietnam.repository;

import com.alsvietnam.entities.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Duc_Huy
 * Date: 8/21/2022
 * Time: 11:59 PM
 */

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, String> {

    List<PaymentAccount> findAllByStatus(boolean status);

}
