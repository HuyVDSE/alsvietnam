package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.DonationCampaign;
import com.alsvietnam.models.search.ParameterSearchCampaign;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.DonationCampaignRepositoryCustom;
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
 * Date: 10/11/2022
 * Time: 12:16 AM
 */

@Repository
@ExtensionMethod(Extensions.class)
public class DonationCampaignRepositoryCustomImpl extends BaseRepositoryCustom implements DonationCampaignRepositoryCustom {

    @Override
    public ListWrapper<DonationCampaign> searchCampaign(ParameterSearchCampaign searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DonationCampaign> query = builder.createQuery(DonationCampaign.class);
        Root<DonationCampaign> root = query.from(DonationCampaign.class);
        // Tạo điều kiện
        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getTitle())) {
            predicates.add(builder.like(builder.lower(root.get("title")), "%" + searchParam.getTitle() + "%"));
        }
        if (searchParam.getDateStartFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dateStart"), searchParam.getDateStartFrom()));
        }
        if (searchParam.getDateStartTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dateStart"), searchParam.getDateStartTo()));
        }
        if (searchParam.getDateEndFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("dateEnd"), searchParam.getDateEndFrom()));
        }
        if (searchParam.getDateEndTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("dateEnd"), searchParam.getDateEndTo()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getStatus())) {
            predicates.add(builder.equal(root.get("status"), searchParam.getStatus()));
        }
        if (searchParam.getExpectedAmountFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("expectedAmount"), searchParam.getExpectedAmountFrom()));
        }
        if (searchParam.getExpectedAmountTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("expectedAmount"), searchParam.getExpectedAmountTo()));
        }
        if (searchParam.getCurrentAmountFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("currentAmount"), searchParam.getCurrentAmountFrom()));
        }
        if (searchParam.getCurrentAmountTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("currentAmount"), searchParam.getCurrentAmountTo()));
        }
        if (searchParam.getActive() != null) {
            predicates.add(builder.equal(root.get("active"), searchParam.getActive()));
        }
        if (searchParam.isDeleted()) {
            predicates.add(builder.equal(root.get("deleted"), searchParam.isDeleted()));
        } else {
            predicates.add(builder.equal(root.get("deleted"), false));
        }
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        // Phân trang
        TypedQuery<DonationCampaign> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(searchParam.getPageSize());
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        List<DonationCampaign> results = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        return ListWrapper.<DonationCampaign>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
