package com.alsvietnam.converter;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.ArticleContent;
import com.alsvietnam.entities.Topic;
import com.alsvietnam.models.dtos.article.ArticleCustom;
import com.alsvietnam.models.dtos.topic.CreateTopicDto;
import com.alsvietnam.models.dtos.topic.UpdateTopicDto;
import com.alsvietnam.models.profiles.TopicProfile;
import com.alsvietnam.models.search.ParameterSearchTopic;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ExtensionMethod(value = Extensions.class)
public class TopicConverter extends BaseConverter {

    public Topic fromCreateDTO(CreateTopicDto topicDto) {
        return Topic.builder()
                .titleVietnamese(topicDto.getTitleVietnamese())
                .topicParentId(topicDto.getTopicParentId())
                .description(topicDto.getDescription())
                .titleEnglish(topicDto.getTitleEnglish())
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .active(topicDto.getActive())
                .build();
    }

    public void fromUpdateDTO(UpdateTopicDto topicDto, Topic topic) {
        topic.setTopicParentId(topicDto.getTopicParentId());
        topic.setTitleEnglish(topicDto.getTitleEnglish());
        topic.setDescription(topicDto.getDescription());
        topic.setTitleVietnamese(topicDto.getTitleVietnamese());
        topic.setUpdatedAt(new Date());
        topic.setUpdatedBy(userService.getUsernameLogin());
        topic.setActive(topicDto.getActive());
        topic.setDeleted(topicDto.getDeleted());
    }

    public List<TopicProfile> toProfiles(List<Topic> topics, ParameterSearchTopic searchParam) {
        return topics.stream().map(topic -> toProfile(topic, searchParam)).collect(Collectors.toList());
    }

    public TopicProfile toProfile(Topic topic, ParameterSearchTopic searchParam) {
        TopicProfile topicProfile = TopicProfile.builder()
                .id(topic.getId())
                .titleEnglish(topic.getTitleEnglish())
                .topicParentId(topic.getTopicParentId())
                .description(topic.getDescription())
                .titleVietnamese(topic.getTitleVietnamese())
                .active(topic.getActive())
                .createdAt(topic.getCreatedAt())
                .createdBy(topic.getCreatedBy())
                .updatedAt(topic.getUpdatedAt())
                .updatedBy(topic.getUpdatedBy())
                .build();

        if (searchParam.isBuildArticles()) {
            List<String> articleIds = articleRepository.findByTopicIdAndStatus(topic.getId(), EnumConst.ArticleStatusEnum.PUBLISHED.name(),
                    PageRequest.of(0, searchParam.getArticleBuildLimit(), Sort.by("createdAt").descending()));
            if (!articleIds.isEmpty()) {
                List<Article> articles = articleRepository.findArticleAndContentsByIdIn(articleIds);
                List<ArticleCustom> articleCustoms = new ArrayList<>();
                for (Article article : articles) {
                    ArticleCustom articleCustom = ArticleCustom.builder()
                            .id(article.getId())
                            .topicId(topic.getId())
                            .build();
                    if (!article.getArticleContents().isNullOrEmpty()) {
                        for (ArticleContent articleContent : article.getArticleContents()) {
                            if (articleContent.getLanguage().equals(EnumConst.LanguageTypeEnum.EN.name())) {
                                articleCustom.setEnglishTitle(articleContent.getTitle());
                            } else {
                                articleCustom.setVietnameseTitle(articleContent.getTitle());
                            }
                        }
                    }
                    articleCustoms.add(articleCustom);
                }
                topicProfile.setArticles(articleCustoms);
            }
        }

        return topicProfile;
    }

    public List<TopicProfile> toProfilesTree(List<TopicProfile> topicProfiles) {
        Map<String, List<TopicProfile>> topicChildMap = topicProfiles.stream()
                .filter(e -> !Extensions.isBlankOrNull(e.getTopicParentId()))
                .collect(Collectors.groupingBy(TopicProfile::getTopicParentId, Collectors.toList()));

        topicProfiles = topicProfiles.stream()
                .filter(e -> Extensions.isBlankOrNull(e.getTopicParentId()))
                .collect(Collectors.toList());
        topicProfiles.forEach(e -> e.setTopicChild(toProfileTree(topicChildMap, e)));
        return topicProfiles;
    }

    private List<TopicProfile> toProfileTree(Map<String, List<TopicProfile>> topicChildMap, TopicProfile topicProfile) {
        List<TopicProfile> topicProfilesChild = topicChildMap.get(topicProfile.getId());
        if (!Extensions.isNullOrEmpty(topicProfilesChild)) {
            for (TopicProfile child : topicProfilesChild) {
                child.setTopicChild(toProfileTree(topicChildMap, child));
            }
        }
        return topicProfilesChild;
    }
}
