package com.alsvietnam.repository;

import com.alsvietnam.entities.Article;
import com.alsvietnam.models.search.ParameterSearchArticle;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface ArticleRepositoryCustom {

    ListWrapper<Article> searchArticle(ParameterSearchArticle parameterSearchArticle);

}
