package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.models.dtos.faq.CategoryFaqDto;
import com.alsvietnam.models.dtos.faq.CreateCategoryFaqDto;
import com.alsvietnam.models.wrapper.StringResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.EnumConst;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 6:56 PM
 */

@RestController
@RequestMapping(Constants.FAQ_CATEGORY_SERVICE)
@Tag(name = "FAQ Category", description = "FAQ Category API")
public class FaqCategoryController extends BaseController {

    @GetMapping
    public List<CategoryFaqDto> getFaqCategories() {
        return faqService.getCategories();
    }

    @PostMapping
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    public CategoryFaqDto createFaqCategory(@RequestBody @Valid CreateCategoryFaqDto categoryFaqDto) {
        return faqService.createCategory(categoryFaqDto);
    }

    @PutMapping
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    public CategoryFaqDto updateFaqCategory(@RequestBody @Valid CategoryFaqDto categoryFaqDto) {
        return faqService.updateCategory(categoryFaqDto);
    }

    @DeleteMapping("/{id}")
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    public StringResponseWrapper deleteFaqCategory(@PathVariable String id) {
        faqService.deleteCategory(id);
        return StringResponseWrapper.builder().status(1).data("Delete faq category successfully").build();
    }

}
