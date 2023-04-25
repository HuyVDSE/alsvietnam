package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.Topic;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.topic.CreateTopicDto;
import com.alsvietnam.models.dtos.topic.UpdateTopicDto;
import com.alsvietnam.models.profiles.TopicProfile;
import com.alsvietnam.models.search.ParameterSearchTopic;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.TopicService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@ExtensionMethod(value = Extensions.class)
public class TopicServiceImpl extends BaseService implements TopicService {

    @Override
    public ListWrapper<TopicProfile> searchTopic(ParameterSearchTopic searchParam) {
        ListWrapper<Topic> wrapper = topicRepository.searchTopic(searchParam);
        List<TopicProfile> topicProfiles = topicConverter.toProfiles(wrapper.getData(), searchParam);
        List<String> topicIds = topicProfiles.stream().map(TopicProfile::getId).collect(Collectors.toList());

        if (searchParam.isBuildTopicTree()) {
            List<Topic> topics = topicRepository.findAllByTopicParentIdInAndDeleted(topicIds, null);
            topicProfiles.addAll(topicConverter.toProfiles(topics, searchParam));
            topicProfiles = topicProfiles.stream().filter(distinctByKey(TopicProfile::getId)).collect(Collectors.toList());
            topicProfiles = topicConverter.toProfilesTree(topicProfiles);
        }
        return ListWrapper.<TopicProfile>builder()
                .data(topicProfiles)
                .total(wrapper.getTotal())
                .maxResult(wrapper.getMaxResult())
                .currentPage(wrapper.getCurrentPage())
                .totalPage(wrapper.getTotalPage())
                .build();
    }

    @Override
    public Topic createTopic(CreateTopicDto topicDto) {
        topicRepository.findByTitleEnglish(topicDto.getTitleEnglish())
                .ifPresent(topic -> {
                    if (topic.getDeleted() == null || topic.getDeleted().equals(Boolean.FALSE)) {
                        throw new ServiceException("Topic with title " + topicDto.getTitleEnglish() + " already existed");
                    }
                });
        String topicParentId = topicDto.getTopicParentId();
        if (!topicParentId.isBlankOrNull() && !topicRepository.existsById(topicParentId)) {
            throw new ServiceException("Topic parent with id " + topicParentId + " not found");
        }
        Topic topic = topicConverter.fromCreateDTO(topicDto);
        topicRepository.save(topic);
        logDataService.create(topic.getId(), EnumConst.LogTypeEnum.TOPIC.name(), "create topic " + topic.getId());
        return topic;
    }

    @Override
    public Topic updateTopic(UpdateTopicDto topicDto) {
        Topic topic = topicRepository.findById(topicDto.getId())
                .orElseThrow(() -> new ServiceException("Topic with id " + topicDto.getId() + " not found"));
        if (!topicDto.getTopicParentId().isBlankOrNull()) {
            Optional<Topic> topicParent = topicRepository.findById(topicDto.getTopicParentId());
            if (topicParent.isEmpty()) {
                throw new ServiceException("Topic parent with id " + topicDto.getTopicParentId() + " not found");
            }
        }
        if (!topic.getTitleEnglish().equals(topicDto.getTitleEnglish())) {
            topicRepository.findByTitleEnglish(topicDto.getTitleEnglish())
                    .ifPresent(topicCurrent -> {
                        if (topicCurrent.getDeleted() == null) {
                            throw new ServiceException("Topic with title " + topicDto.getTitleEnglish() + " already existed");
                        }
                    });
        }
        topicConverter.fromUpdateDTO(topicDto, topic);
        topicRepository.save(topic);
        logDataService.create(topic.getId(), EnumConst.LogTypeEnum.TOPIC.name(), "update topic " + topic.getId());
        return topic;
    }

    @Override
    public void deleteTopic(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ServiceException("Topic not found"));
        List<Topic> topicChild = topicRepository.findAllByTopicParentId(topicId);
        if (topicChild.size() > 0) {
            throw new ServiceException("Topic has child, can't delete", HttpStatus.BAD_REQUEST);
        }
        if (!topic.getArticles().isNullOrEmpty()) {
            for (Article article : topic.getArticles()) {
                if (article.getDeleted() == null) {
                    throw new ServiceException("Topic has article " + article.getId() + ", can't delete", HttpStatus.BAD_REQUEST);
                }
            }
        }
        // write log before delete
        logDataService.create(topic.getId(), EnumConst.LogTypeEnum.TOPIC.name(), "topic_id " + topic.getId() + " deleted");
        topic.setDeleted(true);
        topicRepository.save(topic);
    }

    @Override
    @Transactional
    public void deletePermanentTopic(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ServiceException("Topic not found"));
        Set<Article> articles = topic.getArticles();
        if (articles != null) {
            for (Article article : articles) {
                articleService.deleteAllArticleContent(article.getArticleContents());
                articleService.deleteAllArticleMedia(article.getArticleMedias());
                articleService.deleteAllArticleFile(article.getArticleFiles());
                taskRepository.deleteByArticle_Id(article.getId());
                articleRepository.delete(article);
            }
        }
        // write log before delete
        logDataService.create(topic.getId(), EnumConst.LogTypeEnum.TOPIC.name(), "topic_id " + topic.getId() + " deleted");
        topicRepository.delete(topic);
    }
}