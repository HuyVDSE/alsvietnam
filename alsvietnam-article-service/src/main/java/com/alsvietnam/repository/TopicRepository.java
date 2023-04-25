package com.alsvietnam.repository;

import com.alsvietnam.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, String>, TopicRepositoryCustom {

    List<Topic> findAllByTopicParentId(String topicParentId);

    List<Topic> findAllByTopicParentIdInAndDeleted(List<String> topicParentIds, Boolean deleted);

    Optional<Topic> findByTitleEnglish(String titleEnglish);
}
