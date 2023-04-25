package com.alsvietnam.repository;

import com.alsvietnam.entities.Story;
import com.alsvietnam.models.search.ParameterSearchStory;
import com.alsvietnam.models.wrapper.ListWrapper;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:04 PM
 */

public interface StoryRepositoryCustom {

    ListWrapper<Story> searchStory(ParameterSearchStory parameterSearchStory);
}
