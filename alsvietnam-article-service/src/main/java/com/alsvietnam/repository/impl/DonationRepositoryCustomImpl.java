package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Donation;
import com.alsvietnam.models.search.ParameterSearchDonation;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.DonationRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Duc_Huy
 * Date: 8/23/2022
 * Time: 10:49 PM
 */

@Repository
@ExtensionMethod(value = Extensions.class)
public class DonationRepositoryCustomImpl extends BaseRepositoryCustom implements DonationRepositoryCustom {

    @Override
    public ListWrapper<Donation> searchDonation(ParameterSearchDonation searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Donation> query = builder.createQuery(Donation.class);
        Root<Donation> root = query.from(Donation.class);
        // Tạo điều kiện
        List<Predicate> predicates = new ArrayList<>();
        if (!searchParam.getId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!searchParam.getEmail().isBlankOrNull()) {
            predicates.add(builder.like(root.get("email"), "%" + searchParam.getEmail() + "%"));
        }
        if (!searchParam.getPhone().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("phone"), searchParam.getPhone()));
        }
        if (!searchParam.getPaymentMethod().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("paymentMethod").get("id"), searchParam.getPaymentMethod()));
        }
        if (!searchParam.getTransactionId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("transactionId"), searchParam.getTransactionId()));
        }
        if (!searchParam.getDonationType().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("donationType"), searchParam.getDonationType()));
        }
        if (!searchParam.getDonationCampaignId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("donationCampaign").get("id"), searchParam.getDonationCampaignId()));
        }
        if (searchParam.getCreatedFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), searchParam.getCreatedFrom()));
        }
        if (searchParam.getCreatedTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), searchParam.getCreatedTo()));
        }
        if (searchParam.getPaymentDateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("paymentDate"), searchParam.getPaymentDateFrom()));
        }
        if (searchParam.getPaymentDateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("paymentDate"), searchParam.getPaymentDateTo()));
        }
        if (!searchParam.getStatus().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("status"), searchParam.getStatus()));
        }

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        // Phân trang
        TypedQuery<Donation> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(searchParam.getPageSize());
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        List<Donation> results = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        return ListWrapper.<Donation>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
