package com.alsvietnam.repository;

import com.alsvietnam.entities.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 12:10 AM
 */

public interface FaqRepository extends JpaRepository<Faq, String> {

    List<Faq> findByCategoryFaqId(String categoryId);
}