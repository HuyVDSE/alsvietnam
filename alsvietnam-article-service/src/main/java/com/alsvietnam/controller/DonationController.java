package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Donation;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.payment.*;
import com.alsvietnam.models.profiles.DonationProfile;
import com.alsvietnam.models.search.ParameterSearchDonation;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.models.wrapper.ResponseWrapper;
import com.alsvietnam.service.strategy.payment.PaymentFactory;
import com.alsvietnam.service.strategy.payment.PaymentStrategy;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 11:24 AM
 */

@RestController
@RequestMapping(Constants.DONATION_SERVICE)
@ExtensionMethod(Extensions.class)
@Tag(name = "Donation", description = "Donation API")
@Slf4j
public class DonationController extends BaseController {

    private PaymentFactory paymentFactory;

    @Autowired
    public void setPaymentFactory(PaymentFactory paymentFactory) {
        this.paymentFactory = paymentFactory;
    }

    @GetMapping
    @Operation(summary = "Danh sách donation", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = DonationDto.class))))
    })
    public ListWrapper<DonationDto> getDonations(@RequestParam(value = "id", required = false) String id,
                                                 @RequestParam(value = "email", required = false) String email,
                                                 @RequestParam(value = "status", required = false) String status,
                                                 @RequestParam(value = "phone", required = false) String phone,
                                                 @RequestParam(value = "payment_method", required = false) String paymentMethod,
                                                 @RequestParam(value = "transaction_id", required = false) String transactionId,
                                                 @RequestParam(value = "donation_type", required = false) String donationType,
                                                 @RequestParam(value = "donation_campaign_id", required = false) String donationCampaignId,
                                                 @RequestParam(value = "created_from", required = false) String createdFrom,
                                                 @RequestParam(value = "created_to", required = false) String createdTo,
                                                 @RequestParam(value = "payment_date_from", required = false) String paymentDateFrom,
                                                 @RequestParam(value = "payment_date_to", required = false) String paymentDateTo,
                                                 @RequestParam(value = "sort_by", required = false) String sortBy,
                                                 @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                 @RequestParam(value = "current_page", required = false)
                                                 @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                 @Parameter(description = "Default: 1") Integer currentPage,
                                                 @RequestParam(value = "page_size", required = false)
                                                 @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                 @Max(value = 50, message = "page_size phải bé hơn 50")
                                                 @Parameter(description = "Default: 20") Integer pageSize) {
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchDonation searchParam = new ParameterSearchDonation();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        searchParam.setId(id);
        searchParam.setStatus(status);
        searchParam.setEmail(email);
        searchParam.setPhone(phone);
        searchParam.setPaymentMethod(paymentMethod);
        searchParam.setTransactionId(transactionId);
        searchParam.setDonationType(donationType);
        searchParam.setDonationCampaignId(donationCampaignId);
        searchParam.setCreatedFrom(createdFrom, DateUtil.TYPE_FORMAT_2);
        searchParam.setCreatedTo(createdTo, DateUtil.TYPE_FORMAT_2);
        searchParam.setPaymentDateFrom(paymentDateFrom, DateUtil.TYPE_FORMAT_2);
        searchParam.setPaymentDateTo(paymentDateTo, DateUtil.TYPE_FORMAT_2);
        searchParam.setSortField(sortBy);
        if (!sortOrder.isBlankOrNull()) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }
        searchParam.setStatus(status);
        return donationService.searchDonation(searchParam);
    }

    @GetMapping("/statistic")
    @Operation(summary = "Danh sách donations custom", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = DonationCustomDTO.class))))
    })
    public ListWrapper<DonationCustomDTO> getDonationsCustom(@RequestParam(value = "donation_type", required = false) String donationType,
                                                             @RequestParam(value = "donation_campaign_id", required = false) String donationCampaignId,
                                                             @RequestParam(value = "created_from", required = false) String createdFrom,
                                                             @RequestParam(value = "created_to", required = false) String createdTo,
                                                             @RequestParam(value = "payment_date_from", required = false) String paymentDateFrom,
                                                             @RequestParam(value = "payment_date_to", required = false) String paymentDateTo,
                                                             @RequestParam(value = "build_for_dashboard", required = false) Boolean buildForDashboard,
                                                             @RequestParam(value = "sort_by", required = false) String sortBy,
                                                             @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                             @RequestParam(value = "current_page", required = false)
                                                             @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                             @Parameter(description = "Default: 1") Integer currentPage,
                                                             @RequestParam(value = "page_size", required = false)
                                                             @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                             @Max(value = 50, message = "page_size phải bé hơn 50")
                                                             @Parameter(description = "Default: 20") Integer pageSize) {
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchDonation searchParam = new ParameterSearchDonation();
        searchParam.setDonationCampaignId(donationCampaignId);
        searchParam.setDonationType(donationType);
        searchParam.setCreatedFrom(createdFrom, DateUtil.TYPE_FORMAT_1);
        searchParam.setCreatedTo(createdTo, DateUtil.TYPE_FORMAT_1);
        searchParam.setPaymentDateFrom(paymentDateFrom, DateUtil.TYPE_FORMAT_1);
        searchParam.setPaymentDateTo(paymentDateTo, DateUtil.TYPE_FORMAT_1);
        searchParam.setBuildForDashboard(buildForDashboard);
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        searchParam.setStatus(EnumConst.PaymentStatusEnum.SUCCESS.name());
        searchParam.setSortField(sortBy);
        if (!sortOrder.isBlankOrNull()) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }
        return donationService.searchDonationCustom(searchParam);
    }

    @Operation(summary = "Lấy donation theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = DonationDto.class)))
    })
    @GetMapping("{id}")
    public DonationDto getDonationById(@PathVariable String id) {
        ParameterSearchDonation searchParam = new ParameterSearchDonation();
        searchParam.setId(id);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        ListWrapper<DonationDto> wrapper = donationService.searchDonation(searchParam);
        List<DonationDto> results = wrapper.getData();
        if (results.isNullOrEmpty()) {
            throw new ServiceException("donation không tồn tại");
        }
        return results.get(0);
    }

    @Operation(summary = "Tạo donation", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = DonationProfile.class)))
    })
    @PostMapping
    public DonationProfile createDonation(@RequestBody @Valid CreateDonationDto dto) {
        return donationService.createDonation(dto);
    }

    @Operation(summary = "Cập nhật donation", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = DonationDto.class)))
    })
    @PutMapping("{id}")
    public DonationDto updateDonation(@PathVariable("id") String id,
                                      @RequestBody @Valid DonationDto dto) {
        dto.setId(id);
        return donationService.updateDonation(dto);
    }

    @Operation(summary = "Cập nhật trạng thái donation", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = DonationDto.class)))
    })
    @PutMapping("{id}/status")
    public DonationDto updateDonationStatus(@PathVariable String id,
                                            @RequestParam("status") String status) {
        return donationService.changeStatusDonation(id, status);
    }

    @SneakyThrows
    @GetMapping("/vnpay/ipn")
    public IpnVnPayResponse vnPayCallback(@RequestParam Map<String, String> params) {
        log.info("Receive VNPAY IPN");
        PaymentStrategy vnPayGateway = paymentFactory.createStrategy(EnumConst.PaymentGatewayEnum.VNPAY.name());
        ResponseWrapper<?> wrapper = vnPayGateway.handleIPNCall(params);
        return (IpnVnPayResponse) wrapper.getData();
    }

    @SneakyThrows
    @GetMapping("/callback")
    public ResponseWrapper<?> receiveCallback(@RequestParam Map<String, String> params) {
        // check vnpay callback
        String donationId = params.get("vnp_TxnRef");
        if (!donationId.isBlankOrNull()) {
            log.info("Receive VNPAY callback");
            Thread.sleep(1000); // sleep a bit for system handle payment IPN
            Donation donation = donationRepository.findById(donationId)
                    .orElseThrow(() -> new ServiceException("Donation not found"));
            return ObjectResponseWrapper.builder().status(1)
                    .data(donationConverter.toDTO(donation))
                    .build();
        }
        // check momo callback
        log.error("donation_id not found");
        return ObjectResponseWrapper.builder().status(0)
                .message("donationId is empty")
                .build();
    }
}
