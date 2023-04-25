package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Role;
import com.alsvietnam.models.search.ParameterSearchRole;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.RoleRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleRepositoryCustomImpl extends BaseRepositoryCustom implements RoleRepositoryCustom {

    @Override
    public ListWrapper<Role> searchRole(ParameterSearchRole searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Role> query = builder.createQuery(Role.class);
        Root<Role> root = query.from(Role.class);
        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getName())) {
            predicates.add(builder.like(builder.lower(root.get("name")),
                    "%" + searchParam.getName().toLowerCase() + "%"));
        }
        if (searchParam.getCreateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), searchParam.getCreateTo()));
        }
        if (searchParam.getCreateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), searchParam.getCreateFrom()));
        }

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());
        // phân trang
        TypedQuery<Role> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(searchParam.getPageSize());
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        List<Role> results = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        return ListWrapper.<Role>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
