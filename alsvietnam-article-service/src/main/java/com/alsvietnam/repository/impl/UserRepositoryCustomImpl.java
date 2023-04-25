package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.User;
import com.alsvietnam.models.search.ParameterSearchUser;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.UserRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/6/2022
 * Time: 11:10 PM
 */

@Repository
public class UserRepositoryCustomImpl extends BaseRepositoryCustom implements UserRepositoryCustom {

    @Override
    public ListWrapper<User> searchUsers(ParameterSearchUser searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<User> root = query.from(User.class);
        query.select(root.get("id"));

        List<Predicate> predicates = new ArrayList<>();
        // tạo điều kiện
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isNullOrEmpty(searchParam.getIds())) {
            predicates.add(root.get("id").in(searchParam.getIds()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getUsername())) {
            predicates.add(builder.like(root.get("username"), "%" + searchParam.getUsername() + "%"));
        }
        if (!Extensions.isBlankOrNull(searchParam.getEmail())) {
            predicates.add(builder.like(root.get("email"), "%" + searchParam.getEmail() + "%"));
        }
        if (!Extensions.isBlankOrNull(searchParam.getPhone())) {
            predicates.add(builder.like(root.get("phone"), "%" + searchParam.getPhone() + "%"));
        }
        if (!Extensions.isBlankOrNull(searchParam.getTeamName())) {
            predicates.add(builder.equal(root.join("teams").get("name"), searchParam.getTeamName()));
        }
        if (!Extensions.isNullOrEmpty(searchParam.getTeamIds())) {
            predicates.add(root.join("teams").get("id").in(searchParam.getTeamIds()));
        }
        if (!Extensions.isNullOrEmpty(searchParam.getRoles())) {
            predicates.add(root.get("role").get("id").in(searchParam.getRoles()));
        }
        if (searchParam.getStatus() != null) {
            predicates.add(builder.equal(root.get("status"), searchParam.getStatus()));
        }
        if (searchParam.getApproveStatus() != null) {
            predicates.add(builder.equal(root.get("approveStatus"), searchParam.getApproveStatus()));
        }
        if (searchParam.getCreatedFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), searchParam.getCreatedFrom()));
        }
        if (searchParam.getCreatedTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), searchParam.getCreatedTo()));
        }
        if (searchParam.getDeleted() != null) {
            predicates.add(builder.equal(root.get("deleted"), searchParam.getDeleted()));
        } else {
            predicates.add(builder.or(root.get("deleted").isNull(), builder.equal(root.get("deleted"), false)));
        }
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        // query userIds by condition
        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        TypedQuery<String> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        if (searchParam.getPageSize() != null) {
            typedQuery.setMaxResults(searchParam.getPageSize());
        }
        List<String> userIds = typedQuery.getResultList();

        // query user by Ids
        CriteriaQuery<User> query1 = builder.createQuery(User.class);
        Root<User> root1 = query1.from(User.class);
        query1.where(root1.get("id").in(userIds));
        setSortField(builder, query1, root1, searchParam.getSortField(), searchParam.getDescSort());
        // fetch reference entities
        EntityGraph<User> userGraph = em.createEntityGraph(User.class);
        if (searchParam.isBuildTeam()) {
            userGraph.addAttributeNodes("teams");
        }
        if (searchParam.isBuildRole()) {
            userGraph.addAttributeNodes("role");
        }

        TypedQuery<User> typedQuery1 = em.createQuery(query1);
        typedQuery1.setHint(ENTITY_GRAPH, userGraph);
        List<User> users = typedQuery1.getResultList();

        // đếm tổng số bản ghi
        Long total = getTotalCount(builder, query, root);
        int pageSize = searchParam.getPageSize() == null ? total.intValue() : searchParam.getPageSize();
        if (pageSize == 0) pageSize = 1;
        return ListWrapper.<User>builder()
                .total(total)
                .totalPage((total - 1) / pageSize + 1)
                .maxResult(pageSize)
                .currentPage(searchParam.getStartIndex() / pageSize + 1)
                .data(users)
                .build();
    }
}
