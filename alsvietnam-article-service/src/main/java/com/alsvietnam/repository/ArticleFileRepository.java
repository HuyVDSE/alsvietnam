package com.alsvietnam.repository;

import com.alsvietnam.entities.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, String> {
    ArticleFile findByIdAndArticleId(String id, String articleId);

    List<ArticleFile> findAllByArticle_IdIn(Collection<String> articleIds);

}
