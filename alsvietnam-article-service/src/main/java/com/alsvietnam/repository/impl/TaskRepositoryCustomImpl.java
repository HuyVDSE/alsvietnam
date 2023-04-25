package com.alsvietnam.repository.impl;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.Task;
import com.alsvietnam.models.search.ParameterSearchTask;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.repository.TaskRepositoryCustom;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.Subgraph;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Duc_Huy
 * Date: 9/16/2022
 * Time: 12:21 PM
 */

@Repository
@ExtensionMethod(Extensions.class)
public class TaskRepositoryCustomImpl extends BaseRepositoryCustom implements TaskRepositoryCustom {

    @Override
    public ListWrapper<Task> searchTask(ParameterSearchTask searchParam) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = builder.createQuery(String.class);
        Root<Task> root = query.from(Task.class);
        query.select(root.get("id"));

        // tạo điều kiện
        List<Predicate> predicates = new ArrayList<>();
        if (!searchParam.getId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("id"), searchParam.getId()));
        }
        if (!searchParam.getStatus().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("status"), searchParam.getStatus()));
        }
        if (!searchParam.getStatuses().isNullOrEmpty()) {
            predicates.add(root.get("status").in(searchParam.getStatuses()));
        }
        if (!searchParam.getTeamId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("team").get("id"), searchParam.getTeamId()));
        }
        if (!searchParam.getArticleId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("article").get("id"), searchParam.getArticleId()));
        }
        if (!searchParam.getManagerId().isBlankOrNull()) {
            predicates.add(builder.equal(root.get("manager").get("id"), searchParam.getManagerId()));
        }
        if (!searchParam.getUserId().isBlankOrNull()) {
            predicates.add(builder.equal(root.join("users").get("id"), searchParam.getUserId()));
        }
        if (!searchParam.getFromStartDate().isBlankOrNull()) {
            Date fromStartDate = DateUtil.formatDateString(searchParam.getFromStartDate(), DateUtil.TYPE_FORMAT_2);
            predicates.add(builder.greaterThanOrEqualTo(root.get("startDate"), fromStartDate));
        }
        if (!searchParam.getToStartDate().isBlankOrNull()) {
            Date toStartDate = DateUtil.formatDateString(searchParam.getToStartDate(), DateUtil.TYPE_FORMAT_2);
            predicates.add(builder.lessThanOrEqualTo(root.get("startDate"), toStartDate));
        }
        if (!searchParam.getFromEndDate().isBlankOrNull()) {
            Date fromEndDate = DateUtil.formatDateString(searchParam.getFromEndDate(), DateUtil.TYPE_FORMAT_2);
            predicates.add(builder.greaterThanOrEqualTo(root.get("endDate"), fromEndDate));
        }
        if (!searchParam.getToEndDate().isBlankOrNull()) {
            Date toEndDate = DateUtil.formatDateString(searchParam.getToEndDate(), DateUtil.TYPE_FORMAT_2);
            predicates.add(builder.lessThanOrEqualTo(root.get("endDate"), toEndDate));
        }
        if (searchParam.getDoneTaskFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("actualEndDate"), searchParam.getDoneTaskFrom()));
        }
        if (searchParam.getDoneTaskTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("actualEndDate"), searchParam.getDoneTaskTo()));
        }
        if (searchParam.getCreatedFrom() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), searchParam.getCreatedFrom()));
        }
        if (searchParam.getCreatedTo() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), searchParam.getCreatedTo()));
        }
        setSortField(builder, query, root, searchParam.getSortField(), searchParam.getDescSort());

        // get task Ids by condition
        Predicate[] predicatesArr = predicates.toArray(new Predicate[0]);
        query.where(predicatesArr);
        TypedQuery<String> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult(searchParam.getStartIndex().intValue());
        if (searchParam.getPageSize() != null) {
            typedQuery.setMaxResults(searchParam.getPageSize());
        }
        List<String> taskIds = typedQuery.getResultList();

        // count row
        Long total = getTotalCount(builder, query, root);

        // get task by Ids and fetch other entities reference
        CriteriaQuery<Task> query1 = builder.createQuery(Task.class);
        Root<Task> root1 = query1.from(Task.class);
        query1.where(root1.get("id").in(taskIds));
        setSortField(builder, query1, root1, searchParam.getSortField(), searchParam.getDescSort());
        TypedQuery<Task> typedQuery1 = em.createQuery(query1);
        EntityGraph<Task> taskGraph = em.createEntityGraph(Task.class);
        if (searchParam.isBuildTeam()) {
            taskGraph.addAttributeNodes("team");
        }
        if (searchParam.isBuildManager()) {
            taskGraph.addAttributeNodes("manager");
        }
        if (searchParam.isBuildUsersDoTask()) {
            taskGraph.addAttributeNodes("users");
        }
        if (searchParam.isBuildArticle()) {
            Subgraph<Article> article = taskGraph.addSubgraph("article");
            article.addAttributeNodes("articleFiles", "articleContents", "articleMedias");
        }
        typedQuery1.setHint(ENTITY_GRAPH, taskGraph);
        List<Task> tasks = typedQuery1.getResultList();

        int pageSize = searchParam.getPageSize() == null ? total.intValue() : searchParam.getPageSize();
        if (pageSize == 0) pageSize = 1;
        return ListWrapper.<Task>builder()
                .total(total)
                .totalPage((total - 1) / pageSize + 1)
                .maxResult(pageSize)
                .currentPage(searchParam.getStartIndex() / pageSize + 1)
                .data(tasks)
                .build();
    }

}
