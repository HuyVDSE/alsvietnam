package com.alsvietnam.service.impl;

import com.alsvietnam.entities.CategoryFaq;
import com.alsvietnam.entities.Faq;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.faq.CategoryFaqDto;
import com.alsvietnam.models.dtos.faq.CreateCategoryFaqDto;
import com.alsvietnam.models.dtos.faq.CreateFaqDto;
import com.alsvietnam.models.dtos.faq.FaqDto;
import com.alsvietnam.service.FaqService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 12:26 PM
 */

@Slf4j
@Service
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
public class FaqServiceImpl extends BaseService implements FaqService {

    private final ModelMapper modelMapper;

    @Override
    public List<CategoryFaqDto> getCategories() {
        log.info("Get faq categories");
        List<CategoryFaq> categoryFaqs = categoryFaqRepository.findAll();
        return categoryFaqs.stream().map(categoryFaq -> modelMapper.map(categoryFaq, CategoryFaqDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<FaqDto> getFaqs() {
        log.info("Get faqs");
        List<Faq> faqs = faqRepository.findAll();
        return faqs.stream().map(faq -> {
            FaqDto faqDto = modelMapper.map(faq, FaqDto.class);
            faqDto.setCategoryFaqId(faq.getCategoryFaq().getId());
            return faqDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FaqDto> getFaqByCategory(String categoryId) {
        log.info("Get faqs by category {}", categoryId);
        List<Faq> faqs = faqRepository.findByCategoryFaqId(categoryId);
        return faqs.stream().map(faq -> {
            FaqDto faqDto = modelMapper.map(faq, FaqDto.class);
            faqDto.setCategoryFaqId(faq.getCategoryFaq().getId());
            return faqDto;
        }).collect(Collectors.toList());
    }

    @Override
    public CategoryFaqDto createCategory(CreateCategoryFaqDto categoryFaqDto) {
        log.info("Create faq category: {}", categoryFaqDto.getEnName());
        CategoryFaq categoryFaq = modelMapper.map(categoryFaqDto, CategoryFaq.class);
        categoryFaq.setCreatedBy(userService.getUsernameLogin());
        categoryFaq.setCreatedAt(new Date());
        categoryFaqRepository.save(categoryFaq);
        logDataService.create(categoryFaq.getId(), CategoryFaq.class.getSimpleName(), "Create FAQ category " + categoryFaq.getId() + " success");
        return modelMapper.map(categoryFaq, CategoryFaqDto.class);
    }

    @Override
    public FaqDto createFaq(CreateFaqDto faqDto) {
        log.info("Create faq");
        CategoryFaq categoryFaq = categoryFaqRepository.findById(faqDto.getCategoryFaqId())
                .orElseThrow(() -> new ServiceException("FAQ Category not found"));
        Faq faq = modelMapper.map(faqDto, Faq.class);
        faq.setCategoryFaq(categoryFaq);
        faq.setCreatedAt(new Date());
        faq.setCreatedBy(userService.getUsernameLogin());
        faqRepository.save(faq);
        logDataService.create(faq.getId(), Faq.class.getSimpleName(), "Create FAQ " + faq.getId() + " success");
        return modelMapper.map(faq, FaqDto.class);
    }

    @Override
    public CategoryFaqDto updateCategory(CategoryFaqDto categoryFaqDto) {
        log.info("Update faq category: {}", categoryFaqDto.getId());
        CategoryFaq categoryFaq = categoryFaqRepository.findById(categoryFaqDto.getId())
                .orElseThrow(() -> new ServiceException("FAQ Category not found"));
        modelMapper.map(categoryFaqDto, categoryFaq);
        categoryFaq.setUpdatedAt(new Date());
        categoryFaq.setUpdatedBy(userService.getUsernameLogin());
        categoryFaqRepository.save(categoryFaq);
        logDataService.create(categoryFaq.getId(), CategoryFaq.class.getSimpleName(), "Update FAQ category " + categoryFaq.getId() + " success");
        return modelMapper.map(categoryFaq, CategoryFaqDto.class);
    }

    @Override
    public FaqDto updateFaq(FaqDto faqDto) {
        log.info("Update faq: {}", faqDto.getId());
        Faq faq = faqRepository.findById(faqDto.getId())
                .orElseThrow(() -> new ServiceException("FAQ not found"));
        modelMapper.map(faqDto, faq);
        faq.setUpdatedAt(new Date());
        faq.setUpdatedBy(userService.getUsernameLogin());
        faqRepository.save(faq);
        logDataService.create(faq.getId(), Faq.class.getSimpleName(), "Update FAQ " + faq.getId() + " success");
        return modelMapper.map(faq, FaqDto.class);
    }

    @Override
    public void deleteCategory(String categoryId) {
        CategoryFaq categoryFaq = categoryFaqRepository.findById(categoryId)
                .orElseThrow(() -> new ServiceException("faq category by id " + categoryId + " not found"));
        if (!categoryFaq.getFaqs().isNullOrEmpty()) {
            throw new ServiceException("Please delete all FAQ reference to this category before delete it");
        }

        categoryFaqRepository.delete(categoryFaq);
        logDataService.create(categoryFaq.getId(), CategoryFaq.class.getSimpleName(), "Delete FAQ category " + categoryId + " success");
    }

    @Override
    public void deleteFaq(String faqId) {
        faqRepository.deleteById(faqId);
        logDataService.create(faqId, Faq.class.getSimpleName(), "Delete FAQ " + faqId + " success");
    }
}
