package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Article;
import com.alsvietnam.models.search.ParameterSearchArticle;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.ArticleRepositoryCustom;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@ExtensionMethod(Extensions.class)
public class ArticleRepositoryCustomImpl extends BaseRepositoryCustom implements ArticleRepositoryCustom {

    @Override
    public ListWrapper<Article> searchArticle(ParameterSearchArticle paramSearch) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Article> root = query.from(Article.class);
        query.select(root.get("id"));

        // Tạo điều kiện
        List<Predicate> predicates = new ArrayList<>();
        if (!Extensions.isBlankOrNull(paramSearch.getId())) {
            predicates.add(builder.equal(root.get("id"), paramSearch.getId()));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getTitle())) {
            predicates.add(builder.like(builder.lower(root.join("articleContents").get("title")),
                    "%" + paramSearch.getTitle().toLowerCase() + "%"));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getLabel())) {
            predicates.add(builder.like(builder.lower(root.get("label")), "%" + paramSearch.getLabel().toLowerCase() + "%"));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getAuthor())) {
            predicates.add(builder.like(root.get("author"), "%" + paramSearch.getAuthor() + "%"));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getTranslator())) {
            predicates.add(builder.like(root.get("translator"), "%" + paramSearch.getTranslator() + "%"));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getStatus())) {
            predicates.add(builder.equal(root.get("status"), paramSearch.getStatus()));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getTopicId())) {
            predicates.add(builder.equal(root.get("topic").get("id"), paramSearch.getTopicId()));
        }
        if (paramSearch.getCreateFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), paramSearch.getCreateFrom()));
        }
        if (paramSearch.getCreateTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), paramSearch.getCreateTo()));
        }
        if (!Extensions.isBlankOrNull(paramSearch.getCreatedUser())) {
            predicates.add(builder.equal(root.get("user").get("id"), paramSearch.getCreatedUser()));
        }
        if (paramSearch.getDeleted() != null) {
            predicates.add(builder.equal(root.get("deleted"), paramSearch.getDeleted()));
        } else {
            predicates.add(builder.or(root.get("deleted").isNull(), builder.equal(root.get("deleted"), false)));
        }
        predicates.add(builder.or(builder.notEqual(root.join("topic", JoinType.LEFT).get("deleted"), false),
                builder.isNull(root.join("topic", JoinType.LEFT).get("deleted"))));
        setSortField(builder, query, root, paramSearch.getSortField(), paramSearch.getDescSort());

        // get article Ids by condition
        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        TypedQuery<String> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(paramSearch.getPageSize());
        typedQuery.setFirstResult(paramSearch.getStartIndex().intValue());
        List<String> articleIds = typedQuery.getResultList();

        // tổng số bản ghi
        Long total = getTotalCount(builder, query, root);

        // get article by Ids
        CriteriaQuery<Article> query1 = builder.createQuery(Article.class);
        Root<Article> root1 = query1.from(Article.class);
        query1.where(root1.get("id").in(articleIds));
        setSortField(builder, query1, root1, paramSearch.getSortField(), paramSearch.getDescSort());
        EntityGraph<?> articleGraph = em.getEntityGraph("article-graph");
        TypedQuery<Article> typedQuery1 = em.createQuery(query1);
        typedQuery1.setHint(ENTITY_GRAPH, articleGraph);
        List<Article> articles = typedQuery1.getResultList();

        return ListWrapper.<Article>builder()
                .total(total)
                .totalPage((total - 1) / paramSearch.getPageSize() + 1)
                .maxResult(paramSearch.getPageSize())
                .currentPage(paramSearch.getStartIndex() / paramSearch.getPageSize() + 1)
                .data(articles)
                .build();
    }
}
