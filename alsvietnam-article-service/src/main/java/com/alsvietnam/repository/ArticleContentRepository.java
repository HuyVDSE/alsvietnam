package com.alsvietnam.repository;

import com.alsvietnam.entities.ArticleContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Duc_Huy
 * Date: 9/12/2022
 * Time: 10:24 PM
 */

public interface ArticleContentRepository extends JpaRepository<ArticleContent, String> {

    ArticleContent findByIdAndArticleId(String id, String articleId);

}
