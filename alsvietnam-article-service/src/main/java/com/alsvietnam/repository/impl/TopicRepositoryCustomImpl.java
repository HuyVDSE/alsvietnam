package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Topic;
import com.alsvietnam.models.search.ParameterSearchTopic;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.TopicRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
@ExtensionMethod(value = Extensions.class)
public class TopicRepositoryCustomImpl extends BaseRepositoryCustom implements TopicRepositoryCustom {

    @Override
    public ListWrapper<Topic> searchTopic(ParameterSearchTopic searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Topic> root = query.from(Topic.class);
        query.select(root.get("id"));

        // tạo điều kiện
        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(searchParam.getId())) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getTitleVietnamese())) {
            predicates.add(builder.like(builder.lower(root.get("titleVietnamese")),
                    "%" + searchParam.getTitleVietnamese().toLowerCase() + "%"));
        }
        if (!Extensions.isBlankOrNull(searchParam.getTitleEnglish())) {
            predicates.add(builder.like(builder.lower(root.get("titleEnglish")),
                    "%" + searchParam.getTitleEnglish().toLowerCase() + "%"));
        }
        if (!Extensions.isBlankOrNull(searchParam.getType())) {
            predicates.add(builder.equal(root.get("type"), searchParam.getType()));
        }
        if (!Extensions.isBlankOrNull(searchParam.getTopicParentId())) {
            predicates.add(builder.equal(root.get("topicParentId"), searchParam.getTopicParentId()));
        }
        if (!searchParam.getTopicParentIds().isNullOrEmpty()) {
            predicates.add(root.get("topicParentId").in(searchParam.getTopicParentIds()));
        }
        if (searchParam.getActive() != null) {
            predicates.add(builder.equal(root.get("active"), searchParam.getActive()));
        }
        if (searchParam.isBuildTopicTree()) {
            predicates.add(builder.isNull(root.get("topicParentId")));
        }
        if (searchParam.getDeleted() != null && !searchParam.getDeleted()) {
            predicates.add(builder.equal(root.get("deleted"), searchParam.getDeleted()));
        } else {
            predicates.add(builder.isNull(root.get("deleted")));
        }

        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        String sortField = searchParam.getSortField();
        if (!sortField.isBlankOrNull()) {
            if (searchParam.getDescSort()) {
                query.orderBy(builder.desc(root.get(sortField)));
            } else {
                query.orderBy(builder.asc(root.get(sortField)));
            }
        }

        // get topic Ids by condition
        TypedQuery<String> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        if (searchParam.getPageSize() != null) {
            typedQuery.setMaxResults(searchParam.getPageSize());
        }
        List<String> topicIds = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);

        // get topic by Ids
        CriteriaQuery<Topic> query1 = builder.createQuery(Topic.class);
        Root<Topic> root1 = query1.from(Topic.class);
        List<Predicate> predicates1 = new ArrayList<>();
        predicates1.add(root1.get("id").in(topicIds));
        if (!sortField.isBlankOrNull()) {
            if (searchParam.getDescSort()) {
                query1.orderBy(builder.desc(root1.get(sortField)));
            } else {
                query1.orderBy(builder.asc(root1.get(sortField)));
            }
        }
        EntityGraph<Topic> topicGraph = em.createEntityGraph(Topic.class);
        query1.where(predicates1.toArray(new Predicate[0]));

        TypedQuery<Topic> typedQuery1 = em.createQuery(query1);
        typedQuery1.setHint(ENTITY_GRAPH, topicGraph);
        List<Topic> topics = typedQuery1.getResultList();

        int pageSize = searchParam.getPageSize() == null ? total.intValue() : searchParam.getPageSize();
        if (pageSize == 0) pageSize = 1;
        return ListWrapper.<Topic>builder()
                .total(total)
                .totalPage((total - 1) / pageSize + 1)
                .maxResult(pageSize)
                .currentPage(searchParam.getStartIndex() / pageSize + 1)
                .data(topics)
                .build();
    }
}
