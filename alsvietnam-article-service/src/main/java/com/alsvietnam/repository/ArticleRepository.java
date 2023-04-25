package com.alsvietnam.repository;

import com.alsvietnam.entities.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, String>, ArticleRepositoryCustom {

    @Query(value = "select a.id from Article a " +
            "where a.topic.id = ?1 " +
            "and a.status = ?2 " +
            "and (a.deleted is null or a.deleted = false)")
    List<String> findByTopicIdAndStatus(String topicId, String status, Pageable pageable);

    @Query(value = "select distinct a " +
            "from Article a left join fetch a.articleContents " +
            "where a.id in ?1 ")
    List<Article> findArticleAndContentsByIdIn(List<String> ids);

}
