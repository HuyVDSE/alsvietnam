package com.alsvietnam.repository;

import com.alsvietnam.entities.Topic;
import com.alsvietnam.models.search.ParameterSearchTopic;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface TopicRepositoryCustom {

    ListWrapper<Topic> searchTopic(ParameterSearchTopic parameterSearchTopic);

}
