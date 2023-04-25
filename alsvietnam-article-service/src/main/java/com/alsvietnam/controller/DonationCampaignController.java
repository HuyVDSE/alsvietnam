package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.campaign.CampaignDto;
import com.alsvietnam.models.dtos.campaign.CreateCampaignDto;
import com.alsvietnam.models.search.ParameterSearchCampaign;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Duc_Huy
 * Date: 10/11/2022
 * Time: 12:18 AM
 */

@RestController
@RequestMapping(Constants.DONATION_CAMPAIGN_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "Donation Campaign", description = "Donation Campaign API")
public class DonationCampaignController extends BaseController {

    @GetMapping
    @Operation(summary = "Danh sách donation campaign", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = CampaignDto.class))))
    })
    public ListWrapper<CampaignDto> getDonationCampaigns(@RequestParam(value = "id", required = false) String id,
                                                         @RequestParam(value = "title", required = false) String title,
                                                         @RequestParam(value = "date_start_from", required = false) String dateStartFrom,
                                                         @RequestParam(value = "date_start_to", required = false) String dateStartTo,
                                                         @RequestParam(value = "date_end_from", required = false) String dateEndFrom,
                                                         @RequestParam(value = "date_end_to", required = false) String dateEndTo,
                                                         @RequestParam(value = "expected_amount_from", required = false) String expectedAmountFrom,
                                                         @RequestParam(value = "expected_amount_to", required = false) String expectedAmountTo,
                                                         @RequestParam(value = "current_amount_from", required = false) String currentAmountFrom,
                                                         @RequestParam(value = "current_amount_to", required = false) String currentAmountTo,
                                                         @RequestParam(value = "is_active", required = false) Boolean isActive,
                                                         @RequestParam(value = "is_deleted", required = false) Boolean isDeleted,
                                                         @RequestParam(value = "sort_by", required = false) String sortBy,
                                                         @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                         @RequestParam(value = "current_page", required = false)
                                                         @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                         @Parameter(description = "Default: 1") Integer currentPage,
                                                         @RequestParam(value = "page_size", required = false)
                                                         @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                         @Max(value = 50, message = "page_size phải bé hơn 50")
                                                         @Parameter(description = "Default: 10") Integer pageSize) {
        log.info("GET donation campaign");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchCampaign searchParam = new ParameterSearchCampaign();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        searchParam.setId(id);
        searchParam.setTitle(title);
        searchParam.setDateStartFrom(dateStartFrom, DateUtil.TYPE_FORMAT_1);
        searchParam.setDateStartTo(dateStartTo, DateUtil.TYPE_FORMAT_1);
        searchParam.setDateEndFrom(dateEndFrom, DateUtil.TYPE_FORMAT_1);
        searchParam.setDateEndTo(dateEndTo, DateUtil.TYPE_FORMAT_1);
        searchParam.setExpectedAmountFrom(expectedAmountFrom);
        searchParam.setExpectedAmountTo(expectedAmountTo);
        searchParam.setCurrentAmountFrom(currentAmountFrom);
        searchParam.setCurrentAmountTo(currentAmountTo);
        searchParam.setActive(isActive);
        searchParam.setDeleted(isDeleted != null ? isDeleted : false);
        searchParam.setSortField(sortBy);
        if (!sortOrder.isBlankOrNull()) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }

        return donationCampaignService.searchCampaign(searchParam);
    }

    @Operation(summary = "Lấy donation campaign theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CampaignDto.class)))
    })
    @GetMapping("{id}")
    public CampaignDto getCampaignById(@PathVariable("id") String id) {
        ParameterSearchCampaign searchParam = new ParameterSearchCampaign();
        searchParam.setId(id);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        ListWrapper<CampaignDto> listWrapper = donationCampaignService.searchCampaign(searchParam);
        List<CampaignDto> data = listWrapper.getData();
        if (data.isNullOrEmpty()) {
            throw new ServiceException("Donation Campaign not found");
        }
        return data.get(0);
    }

    @Operation(summary = "Tạo donation campaign", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CampaignDto.class)))
    })
    @PostMapping
    public CampaignDto createCampaign(@RequestBody @Valid CreateCampaignDto dto) {
        return donationCampaignService.createCampaign(dto);
    }

    @Operation(summary = "Cập nhật donation campaign", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CampaignDto.class)))
    })
    @PutMapping("{id}")
    public CampaignDto updateCampaign(@PathVariable("id") String id,
                                      @RequestBody @Valid CampaignDto dto) {
        dto.setId(id);
        return donationCampaignService.updateCampaign(dto);
    }

    @Operation(summary = "Xoá donation campaign", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @DeleteMapping("{id}")
    public ObjectResponseWrapper deleteCampaign(@PathVariable("id") String id) {
        donationCampaignService.disableCampaign(id);
        return ObjectResponseWrapper.builder().status(1)
                .message("Delete campaign success")
                .build();
    }

    @Operation(summary = "Cập nhật campaign active status", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CampaignDto.class)))
    })
    @PutMapping("{id}/active/{active}")
    public CampaignDto updateCampaignActiveStatus(@PathVariable("id") String campaignId,
                                                  @PathVariable("active") boolean isActive) {
        return donationCampaignService.updateActiveStatus(campaignId, isActive);
    }

}
