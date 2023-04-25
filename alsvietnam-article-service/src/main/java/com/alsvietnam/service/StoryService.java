package com.alsvietnam.service;

import com.alsvietnam.models.dtos.story.CreateStoryDto;
import com.alsvietnam.models.dtos.story.StoryDto;
import com.alsvietnam.models.dtos.story.UpdateStoryDto;
import com.alsvietnam.models.search.ParameterSearchStory;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:05 PM
 */
public interface StoryService {

    ListWrapper<StoryDto> searchStory(ParameterSearchStory parameterSearchStory);

    StoryDto createStory(CreateStoryDto createStoryDto);

    StoryDto updateStory(UpdateStoryDto storyDto);

    StoryDto deleteStory(String storyId);
}
