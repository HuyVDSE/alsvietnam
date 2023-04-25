package com.alsvietnam.repository;

import com.alsvietnam.entities.ArticleMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, String> {
    ArticleMedia findByIdAndArticleId(String id, String articleId);

    List<ArticleMedia> findAllByArticle_IdIn(Collection<String> articleIds);

}
