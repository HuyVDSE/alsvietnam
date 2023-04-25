package com.alsvietnam.service;

import com.alsvietnam.models.dtos.faq.CategoryFaqDto;
import com.alsvietnam.models.dtos.faq.CreateCategoryFaqDto;
import com.alsvietnam.models.dtos.faq.CreateFaqDto;
import com.alsvietnam.models.dtos.faq.FaqDto;

import java.util.List;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 12:18 PM
 */

public interface FaqService {

    List<CategoryFaqDto> getCategories();

    List<FaqDto> getFaqs();

    List<FaqDto> getFaqByCategory(String categoryId);

    CategoryFaqDto createCategory(CreateCategoryFaqDto categoryFaqDto);

    FaqDto createFaq(CreateFaqDto faqDto);

    CategoryFaqDto updateCategory(CategoryFaqDto categoryFaqDto);

    FaqDto updateFaq(FaqDto faqDto);

    void deleteCategory(String categoryId);

    void deleteFaq(String faqId);
}
