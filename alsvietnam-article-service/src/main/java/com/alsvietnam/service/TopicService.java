package com.alsvietnam.service;

import com.alsvietnam.entities.Topic;
import com.alsvietnam.models.dtos.topic.CreateTopicDto;
import com.alsvietnam.models.dtos.topic.UpdateTopicDto;
import com.alsvietnam.models.profiles.TopicProfile;
import com.alsvietnam.models.search.ParameterSearchTopic;
import com.alsvietnam.models.wrapper.ListWrapper;

public interface TopicService {

    ListWrapper<TopicProfile> searchTopic(ParameterSearchTopic parameterSearchTopic);

    Topic createTopic(CreateTopicDto topicDto);

    Topic updateTopic(UpdateTopicDto topicDto);

    void deleteTopic(String topicId);

    void deletePermanentTopic(String topicId);

}
