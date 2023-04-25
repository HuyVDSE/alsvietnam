package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.models.dtos.faq.CreateFaqDto;
import com.alsvietnam.models.dtos.faq.FaqDto;
import com.alsvietnam.models.wrapper.StringResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.EnumConst;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Duc_Huy
 * Date: 11/20/2022
 * Time: 6:55 PM
 */

@RestController
@RequestMapping(Constants.FAQ_SERVICE)
@Tag(name = "FAQ", description = "FAQ API")
public class FapController extends BaseController {

    @GetMapping
    public List<FaqDto> getFaqs(@RequestParam(value = "categoryId", required = false) Optional<String> categoryId) {
        if (categoryId.isEmpty()) {
            return faqService.getFaqs();
        } else {
            return faqService.getFaqByCategory(categoryId.get());
        }
    }

    @PostMapping
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    public FaqDto createFaq(@RequestBody @Valid CreateFaqDto faqDto) {
        return faqService.createFaq(faqDto);
    }

    @PutMapping
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    public FaqDto updateFaq(@RequestBody @Valid FaqDto faqDto) {
        return faqService.updateFaq(faqDto);
    }

    @DeleteMapping("/{id}")
    @Secured({
            EnumConst.RoleEnum.MANAGER,
            EnumConst.RoleEnum.ADMIN
    })
    public StringResponseWrapper deleteFaq(@PathVariable String id) {
        faqService.deleteFaq(id);
        return StringResponseWrapper.builder().status(1).data("Delete faq successfully").build();
    }

}
