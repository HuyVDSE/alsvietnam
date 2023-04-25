package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.HonoredTable;
import com.alsvietnam.entities.HonoredUser;
import com.alsvietnam.models.search.ParameterSearchHonoredTable;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.HonoredTableRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Duc_Huy
 * Date: 11/2/2022
 * Time: 9:41 PM
 */

@Repository
@ExtensionMethod(Extensions.class)
public class HonoredTableRepositoryCustomImpl extends BaseRepositoryCustom implements HonoredTableRepositoryCustom {

    @Override
    public ListWrapper<HonoredTable> searchHonoredTable(ParameterSearchHonoredTable searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<HonoredTable> root = query.from(HonoredTable.class);
        query.select(root.get("id"));

        List<Predicate> predicates = new ArrayList<>();
        if (!searchParam.getId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!searchParam.getIds().isNullOrEmpty()) {
            predicates.add(root.get("id").in(searchParam.getIds()));
        }
        if (!searchParam.getTitle().isBlankOrNull()) {
            predicates.add(builder.like(builder.lower(root.get("title")), "%" + searchParam.getTitle().toLowerCase() + "%"));
        }
        if (searchParam.getQuarter() != null) {
            predicates.add(builder.equal(root.get("quarter"), searchParam.getQuarter()));
        }
        if (searchParam.getYear() != null) {
            predicates.add(builder.equal(root.get("year"), searchParam.getYear()));
        }
        if (searchParam.getActive() != null) {
            predicates.add(builder.equal(root.get("active"), searchParam.getActive()));
        }
        if (searchParam.getDeleted() == null) {
            predicates.add(builder.equal(root.get("deleted"), false));
        } else {
            predicates.add(builder.equal(root.get("deleted"), searchParam.getDeleted()));
        }

        // get honored table Ids by condition
        Predicate[] predicatesArr = predicates.toArray(predicates.toArray(new Predicate[0]));
        query.where(predicatesArr);
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());
        TypedQuery<String> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        typedQuery.setMaxResults(searchParam.getPageSize());
        List<String> honoredTableIds = typedQuery.getResultList();

        // count row
        Long totalCount = getTotalCount(builder, query, root);

        // get honored table by Ids and fetch other entities reference
        CriteriaQuery<HonoredTable> query1 = builder.createQuery(HonoredTable.class);
        Root<HonoredTable> root1 = query1.from(HonoredTable.class);
        query1.where(root1.get("id").in(honoredTableIds));
        setSortField(builder, query1, root1, searchParam.getSortField(), searchParam.getDescSort());
        TypedQuery<HonoredTable> typedQuery1 = em.createQuery(query1);
        EntityGraph<?> honoredTableGraph = em.getEntityGraph("honored-table-graph");
        if (searchParam.isBuildUser()) {
            Subgraph<HonoredUser> honoredUsers = honoredTableGraph.addSubgraph("honoredUsers", HonoredUser.class);
            honoredUsers.addAttributeNodes("user");
        }
        typedQuery1.setHint(ENTITY_GRAPH, honoredTableGraph);
        List<HonoredTable> honoredTables = typedQuery1.getResultList();

        return ListWrapper.<HonoredTable>builder()
                .total(totalCount)
                .totalPage((totalCount - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(honoredTables)
                .build();
    }
}
