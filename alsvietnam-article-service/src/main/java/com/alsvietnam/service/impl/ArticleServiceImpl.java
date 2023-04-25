package com.alsvietnam.service.impl;

import com.alsvietnam.entities.*;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.article.ArticleLikeDto;
import com.alsvietnam.models.dtos.article.CreateArticleDto;
import com.alsvietnam.models.dtos.article.UpdateArticleDto;
import com.alsvietnam.models.dtos.article.UpdateBaseArticleCustom;
import com.alsvietnam.models.dtos.article.content.CreateArticleContentDto;
import com.alsvietnam.models.dtos.article.content.CreateArticleContentRequest;
import com.alsvietnam.models.dtos.article.content.UpdateArticleContentDto;
import com.alsvietnam.models.dtos.article.content.UpdateArticleContentRequest;
import com.alsvietnam.models.dtos.article.file.ArticleFileDto;
import com.alsvietnam.models.dtos.article.file.CreateArticleFileRequest;
import com.alsvietnam.models.dtos.article.file.UpdateArticleFileRequest;
import com.alsvietnam.models.dtos.article.media.ArticleMediaDto;
import com.alsvietnam.models.dtos.article.media.CreateArticleMediaRequest;
import com.alsvietnam.models.dtos.article.media.UpdateArticleMediaRequest;
import com.alsvietnam.models.profiles.ArticleProfile;
import com.alsvietnam.models.search.ParameterSearchArticle;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.ArticleService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@ExtensionMethod(value = Extensions.class)
@Slf4j
public class ArticleServiceImpl extends BaseService implements ArticleService {

    @Override
    public ListWrapper<ArticleProfile> searchArticle(ParameterSearchArticle parameterSearchArticle) {
        ListWrapper<Article> wrapper = articleRepository.searchArticle(parameterSearchArticle);

        List<ArticleProfile> articleProfiles = articleConverter.toProfile(wrapper.getData());
        //insert user login is likes article or not
        if (parameterSearchArticle.getLikeArticle() && !articleProfiles.isNullOrEmpty() && !getCurrentUsername().equals("anonymousUser")) {
            Article article = wrapper.getData().get(0);
            User user = getCurrentUser();
            if (article.getUsers().contains(user)) {
                articleProfiles.get(0).setUserId(user.getId());
            }
        }
        return ListWrapper.<ArticleProfile>builder()
                .total(wrapper.getTotal())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .totalPage(wrapper.getTotalPage())
                .data(articleProfiles)
                .build();
    }

    @Override
    @SneakyThrows
    public ArticleProfile createArticle(CreateArticleDto articleDto) {
        Topic topic = getTopicById(articleDto.getTopicId());
        File file = null;
        String url = null;
        if (articleDto.getCoverImage() != null) {
            file = fileService.convertMultipartToFile(articleDto.getCoverImage());
            url = fileStorageService.uploadFile(file, ArticleMedia.class.getSimpleName());
        }

        Article article = articleConverter.fromCreateDTO(articleDto, topic, url, file);
        articleRepository.save(article);

        if (file != null) {
            fileService.deleteFileLocal(file);
        }

        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "create article " + article.getId());
        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleProfile createArticleContent(CreateArticleContentRequest contentDto) {
        Article article = articleRepository.findById(contentDto.getArticleId())
                .orElseThrow(() -> new ServiceException("Article with id " + contentDto.getArticleId() + " not found"));
        Set<ArticleContent> contents = new HashSet<>();
        for (CreateArticleContentDto articleContent : contentDto.getArticleContents()) {
            ArticleContent content = articleConverter.fromCreateContentDTO(articleContent, article);
            contents.add(content);
        }
        if (article.getArticleContents() != null)
            article.getArticleContents().addAll(contents);
        else
            article.setArticleContents(contents);
        articleRepository.save(article);
        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleFileDto createArticleFile(CreateArticleFileRequest articleFileRequest, String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        File file = fileService.convertMultipartToFileWithTimestampName(articleFileRequest.getArticleFile());
        String url = fileStorageService.uploadFile(file, ArticleFile.class.getSimpleName());
        ArticleFile articleFile = articleConverter.fromCreateFileDTO(file, article, articleFileRequest.getLanguage(), url);
        articleFileRepository.save(articleFile);
        fileService.deleteFileLocal(file);
        return articleConverter.toFileDto(articleFile);
    }

    @Override
    public ArticleMediaDto createArticleMedia(CreateArticleMediaRequest articleMediaDto, String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        File file = fileService.convertMultipartToFile(articleMediaDto.getArticleMedia());
        String url = fileStorageService.uploadFile(file, ArticleMedia.class.getSimpleName());
        ArticleMedia articleMedia = articleConverter.fromCreateMediaDTO(file, article, url,
                articleMediaDto.getCoverImage(), articleMediaDto.getIndex());
        articleMediaRepository.save(articleMedia);
        fileService.deleteFileLocal(file);
        return articleConverter.toMediaDto(articleMedia);
    }

    @Override
    public ArticleProfile updateArticle(UpdateArticleDto articleDto) {
        Article article = articleRepository.findById(articleDto.getId())
                .orElseThrow(() -> new ServiceException("Article with id " + articleDto.getId() + " not found"));
        if (!article.getTopic().getId().equals(articleDto.getTopicId())) {
            Topic topic = getTopicById(articleDto.getTopicId());
            article.setTopic(topic);
        }
        article = articleConverter.fromUpdateDTO(articleDto, article);
        articleRepository.save(article);
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "update article " + article.getId());
        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleProfile updateArticle(UpdateBaseArticleCustom articleDto) {
        Article article = articleRepository.findById(articleDto.getId())
                .orElseThrow(() -> new ServiceException("Article with id " + articleDto.getId() + " not found"));
        if (!article.getTopic().getId().equals(articleDto.getArticleDto().getTopicId())) {
            Topic topic = getTopicById(articleDto.getArticleDto().getTopicId());
            article.setTopic(topic);
        }

        article = articleConverter.fromUpdateArticle(articleDto.getArticleDto(), article);
        articleRepository.save(article);
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "update article " + article.getId());
        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleProfile updateArticleContent(UpdateArticleContentRequest model) {
        Article article = articleRepository.findById(model.getArticleId())
                .orElseThrow(() -> new ServiceException("Article with id " + model.getArticleId() + " not found"));
        Map<String, ArticleContent> articleContentMap = article.getArticleContents().stream()
                .collect(Collectors.toMap(ArticleContent::getId, Function.identity()));
        for (UpdateArticleContentDto contentDto : model.getArticleContents()) {
            ArticleContent articleContent = articleContentMap.get(contentDto.getId());
            if (articleContent == null) {
                log.info("Article content with id " + contentDto.getId() + " not found");
                continue;
            }
            articleContent = articleConverter.fromUpdateContentDTO(contentDto, articleContent);
            article.getArticleContents().add(articleContent);
            logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "update article content " + articleContent.getId());
        }

        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleProfile updateArticleFile(UpdateArticleFileRequest model) {
        Article article = articleRepository.findById(model.getArticleId())
                .orElseThrow(() -> new ServiceException("Article with id " + model.getArticleId() + " not found"));
        Map<String, ArticleFile> articleFileMap = article.getArticleFiles().stream()
                .collect(Collectors.toMap(ArticleFile::getId, Function.identity()));
        ArticleFile articleFile = articleFileMap.get(model.getId());
        if (articleFile == null) {
            throw new ServiceException("Article file with id " + model.getId() + " not found");
        }
        articleFile = articleConverter.fromUpdateFileDTO(model, articleFile);
        article.getArticleFiles().add(articleFile);
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "update article file " + articleFile.getId());
        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleProfile updateArticleMedia(UpdateArticleMediaRequest model) {
        Article article = articleRepository.findById(model.getArticleId())
                .orElseThrow(() -> new ServiceException("Article with id " + model.getArticleId() + " not found"));
        Map<String, ArticleMedia> articleMediaMap = article.getArticleMedias().stream()
                .collect(Collectors.toMap(ArticleMedia::getId, Function.identity()));
        ArticleMedia articleMedia = articleMediaMap.get(model.getId());
        if (articleMedia == null) {
            throw new ServiceException("Article media with id " + model.getId() + " not found");
        }
        articleMedia = articleConverter.fromUpdateMediaDTO(model, articleMedia);
        article.getArticleMedias().add(articleMedia);
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "update article media " + articleMedia.getId());
        return articleConverter.toProfile(article);
    }

    @Override
    public ArticleProfile updateArticleStatus(String articleId, String status) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        article.setStatus(status);
        articleRepository.save(article);
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "update article status " + article.getStatus());
        return articleConverter.toProfile(article);
    }

    @Override
    public void deleteArticleContent(String articleId, String contentId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "article_content_id " + contentId + " deleted");
        if (article.getArticleContents() != null) {
            ArticleContent content = articleContentRepository.findByIdAndArticleId(contentId, articleId);
            if (content == null) {
                throw new ServiceException("Article Content with id " + contentId + " not found");
            }
            articleContentRepository.delete(content);
        }
    }

    @Override
    public void deleteArticleFile(String articleId, String fileId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "article_file_id " + fileId + " deleted");
        if (article.getArticleFiles() != null) {
            ArticleFile file = articleFileRepository.findByIdAndArticleId(fileId, articleId);
            if (file == null) {
                throw new ServiceException("Article File with id " + fileId + " not found");
            }
            fileStorageService.deleteFile(file.getName(), ArticleFile.class.getSimpleName());
            articleFileRepository.delete(file);
        }
    }

    @Override
    public void deleteArticleMedia(String articleId, String mediaId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "article_media_id " + mediaId + " deleted");
        if (article.getArticleMedias() != null) {
            ArticleMedia media = articleMediaRepository.findByIdAndArticleId(mediaId, articleId);
            if (media == null) {
                throw new ServiceException("Article Media with id " + mediaId + " not found");
            }
            fileStorageService.deleteFile(media.getName(), ArticleMedia.class.getSimpleName());
            articleMediaRepository.delete(media);
        }
    }


    @Override
    public void deleteArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        logDataService.create(article.getId(), EnumConst.LogTypeEnum.ARTICLE.name(), "article_id " + article.getId() + " deleted");
        article.setDeleted(true);
        article.setUpdatedAt(new Date());
        article.setUpdatedBy(getCurrentUsername());
        articleRepository.save(article);
    }

    @Override
    public void likeArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        User user = userService.getUserLogin();
        if (user == null) {
            throw new ServiceException("you must login before react this article");
        }
        Integer likeNumber = article.getLikeNumber();
        article.setLikeNumber(likeNumber + 1);
        Set<User> users = article.getUsers();
        if (users == null) {
            users = new HashSet<>();
        }
        if (users.contains(user)) {
            throw new ServiceException("you already liked this article");
        }
        users.add(user);

        articleRepository.save(article);
    }

    @Override
    public void unlikeArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        User user = userService.getUserLogin();

        Integer likeNumber = article.getLikeNumber();
        Set<User> likeUsers = article.getUsers();
        if (likeNumber == 0 || likeUsers == null || !likeUsers.contains(user)) {
            throw new ServiceException("User with id " + user.getId() + " not like comment yet");
        }

        article.setLikeNumber(likeNumber - 1);
        likeUsers.remove(user);

        articleRepository.save(article);
    }

    @Override
    public void deleteAllArticleContent(Set<ArticleContent> articleContents) {
        if (articleContents != null) {
            articleContentRepository.deleteAll(articleContents);
        }
    }

    @Override
    public void deleteAllArticleFile(Set<ArticleFile> articleFiles) {
        if (articleFiles != null) {
            articleFiles.forEach(articleFile -> fileStorageService.deleteFile(articleFile.getName(), ArticleFile.class.getSimpleName()));
            articleFileRepository.deleteAll(articleFiles);
        }
    }

    @Override
    public void deleteAllArticleMedia(Set<ArticleMedia> articleMedias) {
        if (articleMedias != null) {
            articleMedias.forEach(articleMedia -> fileStorageService.deleteFile(articleMedia.getName(), ArticleMedia.class.getSimpleName()));
            articleMediaRepository.deleteAll(articleMedias);
        }
    }

    @Override
    public ArticleLikeDto getLikeArticle(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ServiceException("Article with id " + articleId + " not found"));
        String username = userService.getUsernameLogin();
        User user = null;

        if (!username.equals("anonymousUser")) {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ServiceException("username " + username + " not found"));
        }

        Integer likeNumber = article.getLikeNumber();

        ArticleLikeDto articleLikeDto = ArticleLikeDto.builder()
                .likeNumber(likeNumber)
                .build();

        if (article.getUsers().contains(user)) {
            articleLikeDto.setUserId(user.getId());
        }

        return articleLikeDto;
    }

    private Topic getTopicById(String topicId) {
        if (topicId.isBlankOrNull()) {
            throw new ServiceException("Topic id is required");
        }
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new ServiceException("Topic with id " + topicId + " not found"));
    }
}
