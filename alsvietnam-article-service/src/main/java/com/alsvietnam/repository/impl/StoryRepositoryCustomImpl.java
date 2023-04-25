package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Story;
import com.alsvietnam.models.search.ParameterSearchStory;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.StoryRepositoryCustom;
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
 * Date: 10/23/2022
 * Time: 10:37 PM
 */

@Repository
@ExtensionMethod(value = Extensions.class)
public class StoryRepositoryCustomImpl extends BaseRepositoryCustom implements StoryRepositoryCustom {

    @Override
    public ListWrapper<Story> searchStory(ParameterSearchStory searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Story> query = builder.createQuery(Story.class);
        Root<Story> root = query.from(Story.class);

        // create condition
        List<Predicate> predicates = new ArrayList<>();
        if (!searchParam.getId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!searchParam.getTitle().isBlankOrNull()) {
            predicates.add(builder.like(builder.lower(root.get("title")), "%" + searchParam.getTitle() + "%"));
        }
        if (!searchParam.getArticleId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("article").get("id"), searchParam.getArticleId()));
        }
        if (!searchParam.getUserId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("user").get("id"), searchParam.getUserId()));
        }
        if (searchParam.getDeleted() != null) {
            predicates.add(builder.equal(root.get("deleted"), searchParam.getDeleted()));
        } else {
            predicates.add(builder.equal(root.get("deleted"), false));
        }
        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());
        // paging
        TypedQuery<Story> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        typedQuery.setMaxResults(searchParam.getPageSize());
        List<Story> results = typedQuery.getResultList();

        // total record
        Long total = getTotalCount(builder, query, root);

        return ListWrapper.<Story>builder()
                .total(total)
                .totalPage((total - 1) / searchParam.getPageSize() + 1)
                .maxResult(searchParam.getPageSize())
                .currentPage(searchParam.getStartIndex() / searchParam.getPageSize() + 1)
                .data(results)
                .build();
    }
}
