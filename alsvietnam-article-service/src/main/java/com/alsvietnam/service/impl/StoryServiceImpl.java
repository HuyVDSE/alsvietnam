package com.alsvietnam.service.impl;

import com.alsvietnam.entities.Article;
import com.alsvietnam.entities.Story;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.story.CreateStoryDto;
import com.alsvietnam.models.dtos.story.StoryDto;
import com.alsvietnam.models.dtos.story.UpdateStoryDto;
import com.alsvietnam.models.search.ParameterSearchStory;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.service.StoryService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.EnumConst;
import com.alsvietnam.utils.Extensions;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alsvietnam.entities.User;

import java.util.Date;
import java.util.List;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:05 PM
 */

@Service
@ExtensionMethod(Extensions.class)
@Slf4j
public class StoryServiceImpl extends BaseService implements StoryService {


    @Override
    public ListWrapper<StoryDto> searchStory(ParameterSearchStory parameterSearchStory) {
        log.info("search article: {}", parameterSearchStory.toString());
        ListWrapper<Story> wrapper = storyRepository.searchStory(parameterSearchStory);

        List<StoryDto> stories = storyConverter.toDTO(wrapper.getData());
        return ListWrapper.<StoryDto>builder()
                .data(stories)
                .totalPage(wrapper.getTotalPage())
                .currentPage(wrapper.getCurrentPage())
                .maxResult(wrapper.getMaxResult())
                .total(wrapper.getTotal())
                .build();
    }

    @Override
    @Transactional
    public StoryDto createStory(CreateStoryDto createStoryDto) {
        log.info("Create Story: {}", createStoryDto.getTitle());
        User user = userRepository.findById(createStoryDto.getUserId())
                .orElseThrow(() -> new ServiceException("User with id " + createStoryDto.getUserId() + " not found"));
        Story story = storyConverter.fromCreateDto(createStoryDto, user);
        storyRepository.save(story);
        logDataService.create(story.getId(), EnumConst.LogTypeEnum.STORY.name(), "create story " + story.getId());
        return storyConverter.toDTO(story);
    }

    @Override
    @Transactional
    public StoryDto updateStory(UpdateStoryDto storyDto) {
        log.info("Update story: {}", storyDto.getId());
        Story story = storyRepository.findById(storyDto.getId())
                .orElseThrow(() -> new ServiceException("Story with id " + storyDto.getId() + " not found"));
        User user = userRepository.findById(storyDto.getUserId())
                .orElseThrow(() -> new ServiceException("User with id " + storyDto.getUserId() + " not found"));
        storyConverter.fromUpdateDto(story, storyDto);
        if (!storyDto.getArticleId().isBlankOrNull()) {
            Article article = articleRepository.findById(storyDto.getArticleId())
                    .orElseThrow(() -> new ServiceException("Article " + storyDto.getArticleId() + " not found"));
            story.setArticle(article);
        }
        logDataService.create(story.getId(), EnumConst.LogTypeEnum.STORY.name(), "update story " + story.getId());
        return storyConverter.toDTO(story);
    }

    @Override
    public StoryDto deleteStory(String storyId) {
        log.info("Delete story: {}", storyId);
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ServiceException("Story not found"));
        story.setDeleted(true);
        story.setUpdatedBy(getCurrentUsername());
        story.setUpdatedAt(new Date());
        logDataService.create(story.getId(), EnumConst.LogTypeEnum.STORY.name(), "delete story " + story.getId());
        return storyConverter.toDTO(story);
    }
}
