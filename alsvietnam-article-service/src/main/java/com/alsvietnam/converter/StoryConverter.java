package com.alsvietnam.converter;

import com.alsvietnam.entities.Story;
import com.alsvietnam.entities.User;
import com.alsvietnam.models.dtos.story.CreateStoryDto;
import com.alsvietnam.models.dtos.story.StoryDto;
import com.alsvietnam.models.dtos.story.UpdateStoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:16 PM
 */

@Component
@RequiredArgsConstructor
public class StoryConverter extends BaseConverter {

    private final UserConverter userConverter;

    public Story fromCreateDto(CreateStoryDto createStoryDto, User user) {
        Story story = modelMapper.map(createStoryDto, Story.class);
        story.setDeleted(false);
        story.setCreatedAt(new Date());
        story.setCreatedBy(userService.getUsernameLogin());
        story.setUser(user);
        return story;
    }

    public StoryDto toDTO(Story story) {
        StoryDto dto = modelMapper.map(story, StoryDto.class);
        dto.setArticleId(story.getArticle() != null ? story.getArticle().getId() : null);
        dto.setUserId(story.getUser().getId());
        dto.setUserProfile(userConverter.toProfile(story.getUser()));
        return dto;
    }

    public List<StoryDto> toDTO(List<Story> stories) {
        return stories.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public void fromUpdateDto(Story story, UpdateStoryDto storyDto) {
        modelMapper.map(storyDto, story);
        story.setUpdatedAt(new Date());
        story.setUpdatedBy(userService.getUsernameLogin());
    }
}
