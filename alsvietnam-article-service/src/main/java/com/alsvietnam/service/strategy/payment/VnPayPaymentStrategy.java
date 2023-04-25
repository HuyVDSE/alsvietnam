package com.alsvietnam.service.strategy.payment;

import com.alsvietnam.properties.VnPayProperties;
import com.alsvietnam.entities.Donation;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.payment.DonationDto;
import com.alsvietnam.models.dtos.payment.IpnVnPayResponse;
import com.alsvietnam.models.dtos.payment.VnPayCallbackDto;
import com.alsvietnam.models.search.ParameterSearchDonation;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ResponseWrapper;
import com.alsvietnam.service.strategy.BaseStrategy;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EncryptUtil;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Duc_Huy
 * Date: 8/22/2022
 * Time: 5:04 PM
 */

@Service
@RequiredArgsConstructor
@ExtensionMethod(Extensions.class)
@Slf4j
public class VnPayPaymentStrategy extends BaseStrategy implements PaymentStrategy {

    private final VnPayProperties vnPayProperties;

    private final HttpServletRequest request;

    @SneakyThrows
    @Override
    public String createPaymentLink(Donation donation) {
        log.info("Create payment link for " + donation.getId());

        String version = vnPayProperties.getApiVersion();
        String command = "pay";
        String orderType = "270000"; // mã loại hàng hoá: Nhà thuốc - dịch vụ y tế
        String txnRef = donation.getId();
        String ipAddr = getIpAddress();
        String tmnCode = vnPayProperties.getTmnCode();
        String firstName = donation.getFirstName();
        String middleName = donation.getMiddleName();
        String lastName = donation.getLastName();
        String fullName = firstName.trim() + " " + lastName.trim();
        String phone = donation.getPhone();
        String email = donation.getEmail();
        String currency = "VND";
        String orderInfo = fullName + " da ung ho cho ALS Viet Nam " + donation.getAmount() + " " + currency;

        Map<String, Object> params = new HashMap<>();
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", String.valueOf(donation.getAmount().multiply(BigDecimal.valueOf(100))));
        params.put("vnp_CurrCode", currency);
        // vnp_Params.put("vnp_BankCode", bank_code);
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", EncryptUtil.unicode2NoSign(orderInfo));
        params.put("vnp_OrderType", orderType);
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnPayProperties.getReturnUrl());
        params.put("vnp_IpAddr", ipAddr);
        if (!phone.isBlankOrNull()) {
            params.put("vnp_Bill_Mobile", phone);
        }
        if (!email.isBlankOrNull()) {
            params.put("vnp_Bill_Email", email);
        }
        if (firstName.isBlankOrNull()) {
            params.put("vnp_Bill_FirstName", firstName);
        }
        if (lastName.isBlankOrNull()) {
            params.put("vnp_Bill_LastName", (middleName.isBlankOrNull() ? "" : middleName.trim() + " " + lastName.trim()).trim());
        }
        String vnp_CreateDate = DateUtil.convertDateToString(new Date(), "yyyyMMddHHmmss");
        params.put("vnp_Inv_Email", "alsvietnam@gmail.com");
        params.put("vnp_Inv_Company", "Tổ chức ALS Việt Nam");
        params.put("vnp_CreateDate", vnp_CreateDate);

        //Build data to hash and querystring
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = (String) params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();

        String vnp_SecureHash = EncryptUtil.hmacSHA512(vnPayProperties.getHashSecret(), hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return vnPayProperties.getPayUrl() + "?" + queryUrl;
    }

    @Override
    public ResponseWrapper<?> handleIPNCall(Map<String, String> params) {
        IpnVnPayResponse vnPayResponse;
        try {
            VnPayCallbackDto callbackDto = objectMapper.convertValue(params, VnPayCallbackDto.class);
            String donationId = callbackDto.getTxnRef();

            String vnp_SecureHash = params.get("vnp_SecureHash");
            params.remove("vnp_SecureHashType");
            params.remove("vnp_SecureHash");
            String orderInfo = params.get("vnp_OrderInfo");
            params.put("vnp_OrderInfo", URLEncoder.encode(orderInfo, StandardCharsets.UTF_8));
            String signValue = EncryptUtil.hashAllFields(vnPayProperties.getHashSecret(), params);

            if (signValue.equals(vnp_SecureHash)) {
                ParameterSearchDonation searchParam = new ParameterSearchDonation();
                searchParam.setId(donationId);
                searchParam.setStartIndex(0L);
                searchParam.setPageSize(1);
                ListWrapper<DonationDto> wrapper = donationService.searchDonation(searchParam);
                List<DonationDto> results = wrapper.getData();
                DonationDto donation = results.isNullOrEmpty() ? null : results.get(0);
                if (donation == null) {
                    log.error("Donation ID {} not exist", donationId);
                    return ResponseWrapper.<IpnVnPayResponse>builder().status(1)
                            .data(new IpnVnPayResponse("01", "Order not Found"))
                            .build();
                }

                // vnp_Amount is valid (Check vnp_Amount VNPAY returns compared to the amount of the code (vnp_TxnRef) in the Your database).
                boolean checkAmount = donation.getAmount()
                        .compareTo(new BigDecimal(callbackDto.getAmount()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)) == 0;

                String newStatus = convertFromVnPayStatus(callbackDto.getTransactionStatus());
                boolean checkOrderStatus = !donation.getStatus().equals(newStatus);

                if (checkAmount) {
                    if (checkOrderStatus) {
                        log.info("Update status donation {}: {} -> {}", donation.getId(), donation.getStatus(), newStatus);
                        donation.setStatus(newStatus);
                        donation.setPaymentDate(DateUtil.formatDateString(callbackDto.getPayDate(), DateUtil.TYPE_FORMAT_3, DateUtil.TYPE_FORMAT_1));
                        donation.setTransactionId(callbackDto.getTransactionNo());
                        donationService.updateDonation(donation);
                        String statusDescription = getStatusMap().get(callbackDto.getTransactionStatus());
                        log.info(statusDescription);
                        vnPayResponse = new IpnVnPayResponse("00", "Confirm Success");
                    } else {
                        vnPayResponse = new IpnVnPayResponse("02", "Order already confirmed");
                    }
                } else {
                    vnPayResponse = new IpnVnPayResponse("04", "Invalid Amount");
                }
            } else {
                vnPayResponse = new IpnVnPayResponse("97", "Invalid Checksum");
            }
        } catch (Exception e) {
            vnPayResponse = new IpnVnPayResponse("99", "Unknown error");
        }

        log.info(vnPayResponse.toString());
        return ResponseWrapper.<IpnVnPayResponse>builder().status(1)
                .data(vnPayResponse)
                .build();
    }

    private Map<String, String> getStatusMap() {
        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("00", "Giao dịch thành công");
        mapValue.put("01", "Giao dịch chưa hoàn tất");
        mapValue.put("02", "Giao dịch bị lỗi");
        mapValue.put("04", "Giao dịch đảo (Khách hàng đã bị trừ tiền tại Ngân hàng nhưng GD chưa thành công ở VNPAY)");
        mapValue.put("05", "VNPAY đang xử lý giao dịch này (GD hoàn tiền)");
        mapValue.put("06", "VNPAY đã gửi yêu cầu hoàn tiền sang Ngân hàng (GD hoàn tiền)");
        mapValue.put("07", "Giao dịch bị nghi ngờ gian lận");
        mapValue.put("09", "GD Hoàn trả bị từ chối");
        mapValue.put("10", "Đã giao hàng");
        mapValue.put("20", "Giao dịch đã được thanh quyết toán cho merchant");
        return mapValue;
    }

    private String convertFromVnPayStatus(String vnPayStatus) {
        String newStatus;
        switch (vnPayStatus) {
            case "00":
            case "10":
            case "20":
                newStatus = EnumConst.PaymentStatusEnum.SUCCESS.name();
                break;
            case "02":
            case "04":
            case "07":
            case "09":
            case "99":
                newStatus = EnumConst.PaymentStatusEnum.FAILED.name();
                break;
            case "01":
            case "05":
            case "06":
                newStatus = EnumConst.PaymentStatusEnum.PENDING.name();
                break;
            default:
                newStatus = vnPayStatus;
        }
        return newStatus;
    }

    private String getIpAddress() {
        String ipAddress;
        try {
            ipAddress = this.request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = this.request.getRemoteAddr();
            }
        } catch (ServiceException e) {
            ipAddress = "Invalid IP:" + e.getMessage();
        }
        return ipAddress;
    }
}
