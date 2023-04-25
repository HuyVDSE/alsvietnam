package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.TopOrganizationSupport;
import com.alsvietnam.models.search.ParameterSearchTopOrganizationSupport;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.TopOrganizationSupportRepositoryCustom;
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

@Repository
@ExtensionMethod(Extensions.class)
public class TopOrganizationSupportRepositoryCustomImpl extends BaseRepositoryCustom implements TopOrganizationSupportRepositoryCustom {
    @Override
    public ListWrapper<TopOrganizationSupport> searchTopOrganizationSupport(ParameterSearchTopOrganizationSupport searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TopOrganizationSupport> query = builder.createQuery(TopOrganizationSupport.class);
        Root<TopOrganizationSupport> root = query.from(TopOrganizationSupport.class);

        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getOrganizationName())) {
            predicates.add(builder.like(builder.lower(root.get("organizationName")),
                    "%" + searchParam.getOrganizationName().toLowerCase() + "%"));
        }
        if (searchParam.getActive() != null) {
            predicates.add(builder.equal(root.get("active"), searchParam.getActive()));
        }
        if (searchParam.getDeleted() != null) {
            predicates.add(builder.equal(root.get("deleted"), searchParam.getDeleted()));
        }
//        else {
//            predicates.add(builder.or(root.get("deleted").isNull(), builder.equal(root.get("deleted"), false)));
//        }
        if (searchParam.getCreateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), searchParam.getCreateTo()));
        }
        if (searchParam.getCreateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), searchParam.getCreateFrom()));
        }

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        // Phân trang
        TypedQuery<TopOrganizationSupport> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(searchParam.getPageSize());
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        List<TopOrganizationSupport> results = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        return ListWrapper.<TopOrganizationSupport>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
