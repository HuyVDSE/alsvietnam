package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Topic;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.topic.CreateTopicDto;
import com.alsvietnam.models.dtos.topic.UpdateTopicDto;
import com.alsvietnam.models.profiles.TopicProfile;
import com.alsvietnam.models.search.ParameterSearchTopic;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(Constants.TOPIC_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "Topic", description = "Topic API")
public class TopicController extends BaseController {

    @SneakyThrows
    @Operation(summary = "Danh sách topic", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopicProfile.class))))
    })
    @GetMapping
    public ListWrapper<TopicProfile> getTopics(@RequestParam(value = "id", required = false) String id,
                                               @RequestParam(value = "title_vietnamese", required = false) String titleVietnamese,
                                               @RequestParam(value = "title_english", required = false) String titleEnglish,
                                               @RequestParam(value = "type", required = false) String type,
                                               @RequestParam(value = "topic_parent_id", required = false) String topicParentId,
                                               @RequestParam(value = "build_tree", required = false) Boolean buildTree,
                                               @RequestParam(value = "build_article", required = false) Boolean buildArticles,
                                               @RequestParam(value = "article_build_limit", required = false) Integer articleBuildLimit,
                                               @RequestParam(value = "sort_by", required = false) String sortBy,
                                               @RequestParam(value = "active", required = false) Boolean active,
                                               @RequestParam(value = "deleted", required = false) Boolean deleted,
                                               @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                               @RequestParam(value = "current_page", required = false)
                                               @Min(value = 1, message = "current_page phải lớn hơn 0")
                                               @Parameter(description = "Default: 1") Integer currentPage,
                                               @RequestParam(value = "page_size", required = false)
                                               @Min(value = 1, message = "page_size phải lớn hơn 0")
                                               @Max(value = 50, message = "page_size phải bé hơn 50")
                                               @Parameter(description = "Default: 10") Integer pageSize) {
        log.info("Get topics");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchTopic searchParam = new ParameterSearchTopic();
        searchParam.setId(id);
        searchParam.setTitleVietnamese(titleVietnamese);
        searchParam.setTitleEnglish(titleEnglish);
        searchParam.setType(type);
        searchParam.setTopicParentId(topicParentId);
        searchParam.setBuildTopicTree(buildTree != null && buildTree);
        searchParam.setArticleBuildLimit(articleBuildLimit);
        searchParam.setActive(active);
        searchParam.setDeleted(deleted);
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        searchParam.setSortField(sortBy);
        searchParam.setBuildArticles(buildArticles != null && buildArticles);
        if (!sortOrder.isBlankOrNull()) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }

        return topicService.searchTopic(searchParam);
    }

    @Operation(summary = "Lấy topic theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TopicProfile.class)))
    })
    @GetMapping("{id}")
    public TopicProfile getTopicById(@PathVariable("id") String id,
                                     @RequestParam(value = "build_tree", required = false) Boolean buildTree) {
        ParameterSearchTopic parameterSearch = new ParameterSearchTopic();
        parameterSearch.setId(id);
        parameterSearch.setBuildTopicTree(buildTree != null && buildTree);
        parameterSearch.setStartIndex(0L);
        parameterSearch.setPageSize(1);
        ListWrapper<TopicProfile> wrapper = topicService.searchTopic(parameterSearch);
        List<TopicProfile> data = wrapper.getData();
        if (data.isNullOrEmpty()) {
            throw new ServiceException("Topic not found");
        }
        return data.get(0);
    }

    @Operation(summary = "Tạo topic", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Topic.class)))
    })
    @PostMapping
    public Topic createTopic(@RequestBody @Valid CreateTopicDto topicDto) {
        log.info("Create topic");
        return topicService.createTopic(topicDto);
    }

    @Operation(summary = "Cập nhật topic", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Topic.class)))
    })
    @PutMapping
    public Topic updateTopic(@RequestBody @Valid UpdateTopicDto topicDto) {
        log.info("Update topic {}", topicDto.getId());
        return topicService.updateTopic(topicDto);
    }

    @Operation(summary = "Xoá topic")
    @DeleteMapping("{id}")
    public void deleteTopic(@PathVariable("id") String id) {
        log.info("Delete topic {}", id);
        topicService.deleteTopic(id);
    }

    @Operation(summary = "Xoá topic vĩnh viễn", hidden = true)
    @DeleteMapping("/{id}/permanent")
    public void deletePermanentTopic(@PathVariable("id") String id) {
        log.info("Delete topic {}", id);
        topicService.deletePermanentTopic(id);
    }
}
