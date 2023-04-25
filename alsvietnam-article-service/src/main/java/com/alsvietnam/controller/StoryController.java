package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.story.CreateStoryDto;
import com.alsvietnam.models.dtos.story.StoryDto;
import com.alsvietnam.models.dtos.story.UpdateStoryDto;
import com.alsvietnam.models.search.ParameterSearchStory;
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
import lombok.experimental.ExtensionMethod;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Duc_Huy
 * Date: 10/23/2022
 * Time: 10:50 PM
 */

@RestController
@RequestMapping(Constants.STORY_SERVICE)
@ExtensionMethod(Extensions.class)
@Tag(name = "Story", description = "Story API")
public class StoryController extends BaseController {

    @Operation(summary = "Xem danh sách story", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = StoryDto.class))))
    })
    @GetMapping
    public ListWrapper<StoryDto> getStories(@RequestParam(value = "id", required = false) String id,
                                            @RequestParam(value = "title", required = false) String title,
                                            @RequestParam(value = "article_id", required = false) String articleId,
                                            @RequestParam(value = "user_id", required = false) String userId,
                                            @RequestParam(value = "deleted", required = false) Boolean deleted,
                                            @RequestParam(value = "current_page", required = false)
                                            @Min(value = 1, message = "current_page phải lớn hơn 0")
                                            @Parameter(description = "Default: 1") Integer currentPage,
                                            @RequestParam(value = "page_size", required = false)
                                            @Min(value = 1, message = "page_size phải lớn hơn 0")
                                            @Max(value = 50, message = "page_size phải bé hơn 50")
                                            @Parameter(description = "Default: 10") Integer pageSize) {
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchStory parameterSearch = new ParameterSearchStory();
        parameterSearch.setPageSize(pageSize);
        parameterSearch.setStartIndex(startIndex);
        parameterSearch.setId(id);
        parameterSearch.setTitle(title);
        parameterSearch.setArticleId(articleId);
        parameterSearch.setUserId(userId);
        parameterSearch.setDeleted(deleted);
        return storyService.searchStory(parameterSearch);
    }

    @Operation(summary = "Lấy story theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = StoryDto.class)))
    })
    @GetMapping("{id}")
    public StoryDto getStoryById(@PathVariable("id") String storyId) {
        ParameterSearchStory parameterSearch = new ParameterSearchStory();
        parameterSearch.setId(storyId);
        parameterSearch.setStartIndex(0L);
        parameterSearch.setPageSize(1);
        ListWrapper<StoryDto> wrapper = storyService.searchStory(parameterSearch);
        List<StoryDto> data = wrapper.getData();
        if (data.isNullOrEmpty()) {
            throw new ServiceException("Story not found");
        }
        return data.get(0);
    }

    @Operation(summary = "Tạo story", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = StoryDto.class)))
    })
    @PostMapping
    public StoryDto createStory(@RequestBody @Valid CreateStoryDto createStoryDto) {
        return storyService.createStory(createStoryDto);
    }

    @Operation(summary = "Cập nhật story", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = StoryDto.class)))
    })
    @PutMapping("{id}")
    public StoryDto updateStory(@PathVariable("id") String storyId,
                                @RequestBody @Valid UpdateStoryDto storyDto) {
        return storyService.updateStory(storyDto);
    }

    @Operation(summary = "Xoá story", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = StoryDto.class)))
    })
    @DeleteMapping("{id}")
    public StoryDto deleteStory(@PathVariable("id") String storyId) {
        return storyService.deleteStory(storyId);
    }
}
