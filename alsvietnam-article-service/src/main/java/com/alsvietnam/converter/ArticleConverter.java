package com.alsvietnam.converter;

import com.alsvietnam.entities.*;
import com.alsvietnam.models.dtos.article.CreateArticleDto;
import com.alsvietnam.models.dtos.article.UpdateArticleDto;
import com.alsvietnam.models.dtos.article.content.ArticleContentDto;
import com.alsvietnam.models.dtos.article.content.CreateArticleContentDto;
import com.alsvietnam.models.dtos.article.content.UpdateArticleContentDto;
import com.alsvietnam.models.dtos.article.file.ArticleFileDto;
import com.alsvietnam.models.dtos.article.file.UpdateArticleFileRequest;
import com.alsvietnam.models.dtos.article.media.ArticleMediaDto;
import com.alsvietnam.models.dtos.article.media.UpdateArticleMediaRequest;
import com.alsvietnam.models.dtos.article.UpdateBaseArticleDto;
import com.alsvietnam.models.profiles.ArticleProfile;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ExtensionMethod(Extensions.class)
public class ArticleConverter extends BaseConverter {

    @SneakyThrows
    public Article fromCreateDTO(CreateArticleDto articleDto, Topic topic, String url, File file) {
        Article article = Article.builder()
                .author(articleDto.getAuthor())
                .translator(articleDto.getTranslator())
                .label(articleDto.getLabel())
                .status(EnumConst.ArticleStatusEnum.DRAFT.name())
                .topic(topic)
                .likeNumber(0)
                .user(userService.getUserLogin())
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .deleted(false)
                .build();

        Set<ArticleMedia> articleMedia = new HashSet<>();
        if (file != null)
            articleMedia.add(fromCreateMediaDTO(file, article, url, true, null));
        article.setArticleMedias(articleMedia);

        return article;
    }

    public ArticleContent fromCreateContentDTO(CreateArticleContentDto articleContentDto, Article article) {
        return ArticleContent.builder()
                .title(articleContentDto.getTitle())
                .description(articleContentDto.getDescription())
                .content(articleContentDto.getContent())
                .language(articleContentDto.getLanguage())
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .article(article)
                .build();
    }

    @SneakyThrows
    public ArticleFile fromCreateFileDTO(File file, Article article, String language, String url) {
        String fileName = file.getName();
        String mime = tika.detect(file);
        return ArticleFile.builder()
                .name(fileName)
                .url(url)
                .extension(Extensions.getExtension(fileName))
                .mime(mime)
                .type(Extensions.getType(mime))
                .language(language)
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .article(article)
                .build();
    }

    @SneakyThrows
    public ArticleMedia fromCreateMediaDTO(File file, Article article, String url, Boolean coverImage, Integer index) {
        String fileName = file.getName();
        String mime = tika.detect(file);
        return ArticleMedia.builder()
                .name(fileName)
                .url(url)
                .extension(Extensions.getExtension(fileName))
                .mime(mime)
                .type(Extensions.getType(mime))
                .coverImage(coverImage)
                .index(index)
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .article(article)
                .build();
    }

    public Article fromUpdateDTO(UpdateArticleDto articleDto, Article article) {
        article.setAuthor(articleDto.getAuthor());
        article.setLabel(articleDto.getLabel());
        article.setStatus(articleDto.getStatus());
        article.setTranslator(articleDto.getTranslator());
        article.setUpdatedAt(new Date());
        article.setUpdatedBy(userService.getUsernameLogin());
        Map<String, ArticleContent> articleContentMap = new HashMap<>();
        article.getArticleContents().forEach(articleContent -> articleContentMap.put(articleContent.getId(), articleContent));
        Set<ArticleContent> articleContents = articleDto.getArticleContents().stream()
                .map(contentDto -> fromUpdateContentDTO(contentDto, articleContentMap.get(contentDto.getId())))
                .collect(Collectors.toSet());
        article.setArticleContents(articleContents);

        if (articleDto.getArticleMedias() != null) {
            Map<String, ArticleMedia> articleMediaMap = new HashMap<>();
            article.getArticleMedias().forEach(articleMedia -> articleMediaMap.put(articleMedia.getId(), articleMedia));
            Set<ArticleMedia> articleMedias = articleDto.getArticleMedias().stream()
                    .map(mediaDto -> fromUpdateMediaDTO(mediaDto, articleMediaMap.get(mediaDto.getId())))
                    .collect(Collectors.toSet());
            article.setArticleMedias(articleMedias);
        }

        if (articleDto.getArticleFiles() != null) {
            Map<String, ArticleFile> articleFileMap = new HashMap<>();
            article.getArticleFiles().forEach(articleFile -> articleFileMap.put(articleFile.getId(), articleFile));
            Set<ArticleFile> articleFiles = articleDto.getArticleFiles().stream()
                    .map(fileDto -> fromUpdateFileDTO(fileDto, articleFileMap.get(fileDto.getId())))
                    .collect(Collectors.toSet());
            article.setArticleFiles(articleFiles);
        }

        return article;
    }

    public Article fromUpdateArticle(UpdateBaseArticleDto articleDto, Article article) {
        article.setAuthor(articleDto.getAuthor());
        article.setLabel(articleDto.getLabel());
        article.setStatus(articleDto.getStatus());
        article.setTranslator(articleDto.getTranslator());
        article.setUpdatedAt(new Date());
        article.setUpdatedBy(userService.getUsernameLogin());
        return article;
    }

    public ArticleContent fromUpdateContentDTO(UpdateArticleContentDto contentDto, ArticleContent articleContent) {
        articleContent.setContent(contentDto.getContent());
        articleContent.setTitle(contentDto.getTitle());
        articleContent.setDescription(contentDto.getDescription());
        articleContent.setLanguage(contentDto.getLanguage());
        articleContent.setUpdatedAt(new Date());
        articleContent.setUpdatedBy(userService.getUsernameLogin());
        return articleContent;
    }

    @SneakyThrows
    public ArticleMedia fromUpdateMediaDTO(UpdateArticleMediaRequest mediaDto, ArticleMedia articleMedia) {
        File file = fileService.convertMultipartToFile(mediaDto.getArticleMedia());
        String url = updateUrl(file, ArticleMedia.class.getSimpleName(), articleMedia.getName());
        String mime = tika.detect(file);

        articleMedia.setName(file.getName());
        articleMedia.setUrl(url);
        articleMedia.setMime(mime);
        articleMedia.setType(Extensions.getType(mime));
        articleMedia.setExtension(Extensions.getExtension(file.getName()));
        articleMedia.setCoverImage(mediaDto.getCoverImage());
        articleMedia.setIndex(mediaDto.getIndex());
        articleMedia.setUpdatedAt(new Date());
        articleMedia.setUpdatedBy(userService.getUsernameLogin());

        fileService.deleteFileLocal(file);
        return articleMedia;
    }

    @SneakyThrows
    public ArticleFile fromUpdateFileDTO(UpdateArticleFileRequest fileDto, ArticleFile articleFile) {
        File file = fileService.convertMultipartToFileWithTimestampName(fileDto.getArticleFile());
        String url = updateUrl(file, ArticleFile.class.getSimpleName(), articleFile.getName());
        String mime = tika.detect(file);

        articleFile.setName(file.getName());
        articleFile.setUrl(url);
        articleFile.setMime(mime);
        articleFile.setType(Extensions.getType(mime));
        articleFile.setExtension(Extensions.getExtension(file.getName()));
        articleFile.setLanguage(fileDto.getLanguage());
        articleFile.setUpdatedAt(new Date());
        articleFile.setUpdatedBy(userService.getUsernameLogin());

        fileService.deleteFileLocal(file);
        return articleFile;
    }

    private String updateUrl(File file, String destination, String filename) {
        fileStorageService.deleteFile(filename, destination);
        return fileStorageService.uploadFile(file, destination);
    }

    public List<ArticleProfile> toProfile(List<Article> articles) {
        return articles.stream().map(this::toProfile).collect(Collectors.toList());
    }

    public ArticleProfile toProfile(Article article) {
        ArticleMediaDto coverImage = null;

        if (!Extensions.isNullOrEmpty(article.getArticleMedias())) {
            for (ArticleMedia articleMedia : article.getArticleMedias()) {
                if (Boolean.TRUE.equals(articleMedia.getCoverImage())) coverImage = toMediaDto(articleMedia);
            }
        }
        List<ArticleContentDto> contents = null;
        if (!Extensions.isNullOrEmpty(article.getArticleContents())) {
            contents = article.getArticleContents().stream()
                    .map(articleContent -> toContentDto(articleContent, article.getId()))
                    .sorted(Comparator.comparing(CreateArticleContentDto::getLanguage))
                    .collect(Collectors.toList());
        }

        List<ArticleFileDto> files = null;
        if (!Extensions.isNullOrEmpty(article.getArticleFiles())) {
            files = article.getArticleFiles().stream()
                    .map(this::toFileDto)
                    .collect(Collectors.toList());
        }

        List<ArticleMediaDto> medias = null;
        if (!Extensions.isNullOrEmpty(article.getArticleMedias())) {
            medias = article.getArticleMedias().stream()
                    .map(this::toMediaDto)
                    .collect(Collectors.toList());
        }

        return ArticleProfile.builder()
                .id(article.getId())
                .author(article.getAuthor())
                .label(article.getLabel())
                .status(article.getStatus())
                .translator(article.getTranslator())
                .topicId(article.getTopic().getId())
                .likeNumber(article.getLikeNumber())
                .createdUserId(article.getUser() != null ? article.getUser().getId() : null)
                .coverImage(coverImage)
                .articleContents(contents)
                .articleFiles(files)
                .articleMedias(medias)
                .createdAt(article.getCreatedAt())
                .createdBy(article.getCreatedBy())
                .updatedAt(article.getUpdatedAt())
                .updatedBy(article.getUpdatedBy())
                .build();
    }

    public ArticleContentDto toContentDto(ArticleContent articleContent, String articleId) {
        return ArticleContentDto.builder()
                .id(articleContent.getId())
                .content(articleContent.getContent())
                .title(articleContent.getTitle())
                .description(articleContent.getDescription())
                .language(articleContent.getLanguage())
                .articleId(articleId)
                .createdAt(articleContent.getCreatedAt())
                .createdBy(articleContent.getCreatedBy())
                .updatedAt(articleContent.getUpdatedAt())
                .updatedBy(articleContent.getUpdatedBy())
                .build();
    }

    public ArticleFileDto toFileDto(ArticleFile articleFile) {
        return ArticleFileDto.builder()
                .id(articleFile.getId())
                .name(articleFile.getName())
                .url(articleFile.getUrl())
                .extension(articleFile.getExtension())
                .mime(articleFile.getMime())
                .type(articleFile.getType())
                .language(articleFile.getLanguage())
                .articleId(articleFile.getArticle().getId())
                .createdAt(articleFile.getCreatedAt())
                .createdBy(articleFile.getCreatedBy())
                .updatedAt(articleFile.getUpdatedAt())
                .updatedBy(articleFile.getUpdatedBy())
                .build();
    }

    public ArticleMediaDto toMediaDto(ArticleMedia articleMedia) {
        return ArticleMediaDto.builder()
                .id(articleMedia.getId())
                .name(articleMedia.getName())
                .url(articleMedia.getUrl())
                .extension(articleMedia.getExtension())
                .mime(articleMedia.getMime())
                .type(articleMedia.getType())
                .coverImage(articleMedia.getCoverImage())
                .index(articleMedia.getIndex())
                .articleId(articleMedia.getArticle().getId())
                .createdAt(articleMedia.getCreatedAt())
                .createdBy(articleMedia.getCreatedBy())
                .updatedAt(articleMedia.getUpdatedAt())
                .updatedBy(articleMedia.getUpdatedBy())
                .build();
    }
}
