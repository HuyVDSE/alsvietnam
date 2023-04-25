package com.alsvietnam.service;

import com.alsvietnam.entities.ArticleContent;
import com.alsvietnam.entities.ArticleFile;
import com.alsvietnam.entities.ArticleMedia;
import com.alsvietnam.models.dtos.article.ArticleLikeDto;
import com.alsvietnam.models.dtos.article.CreateArticleDto;
import com.alsvietnam.models.dtos.article.UpdateArticleDto;
import com.alsvietnam.models.dtos.article.UpdateBaseArticleCustom;
import com.alsvietnam.models.dtos.article.content.CreateArticleContentRequest;
import com.alsvietnam.models.dtos.article.content.UpdateArticleContentRequest;
import com.alsvietnam.models.dtos.article.file.*;
import com.alsvietnam.models.dtos.article.media.*;
import com.alsvietnam.models.profiles.ArticleProfile;
import com.alsvietnam.models.search.ParameterSearchArticle;
import com.alsvietnam.models.wrapper.ListWrapper;

import java.util.Set;

public interface ArticleService {

    ListWrapper<ArticleProfile> searchArticle(ParameterSearchArticle parameterSearchArticle);

    ArticleProfile createArticle(CreateArticleDto articleDto);

    ArticleProfile createArticleContent(CreateArticleContentRequest contentDto);

    ArticleFileDto createArticleFile(CreateArticleFileRequest fileDto, String articleId);

    ArticleMediaDto createArticleMedia(CreateArticleMediaRequest mediaDto, String articleId);

    ArticleProfile updateArticle(UpdateArticleDto articleDto);

    ArticleProfile updateArticle(UpdateBaseArticleCustom articleDto);

    ArticleProfile updateArticleContent(UpdateArticleContentRequest contents);

    ArticleProfile updateArticleFile(UpdateArticleFileRequest files);

    ArticleProfile updateArticleMedia(UpdateArticleMediaRequest medias);

    ArticleProfile updateArticleStatus(String articleId, String status);

    void deleteArticleContent(String articleId, String contentId);

    void deleteArticleFile(String articleId, String fileId);

    void deleteArticleMedia(String articleId, String mediaId);

    void deleteArticle(String articleId);

    void likeArticle(String articleId);

    void unlikeArticle(String articleId);

    void deleteAllArticleContent(Set<ArticleContent> articleContents);

    void deleteAllArticleFile(Set<ArticleFile> articleFiles);

    void deleteAllArticleMedia(Set<ArticleMedia> articleMedia);

    ArticleLikeDto getLikeArticle(String articleId);
}
