package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.comment.CreateCommentDto;
import com.alsvietnam.models.dtos.comment.ReportCommentDto;
import com.alsvietnam.models.dtos.comment.UpdateCommentDto;
import com.alsvietnam.models.profiles.CommentProfile;
import com.alsvietnam.models.search.ParameterSearchComment;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Constants.COMMENT_SERVICE)
@ExtensionMethod(Extensions.class)
@Tag(name = "Comment", description = "Comment API")
@Slf4j
public class CommentController extends BaseController {

    @Operation(summary = "Danh sách comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @GetMapping
    public ListWrapper<CommentProfile> getComments(@RequestParam(value = "id", required = false) String id,
                                                   @RequestParam(value = "content", required = false) String content,
                                                   @RequestParam(value = "flag", required = false) @Parameter(description = "Allowed values: true | false.") Boolean flag,
                                                   @RequestParam(value = "createFrom", required = false) Long createFrom,
                                                   @RequestParam(value = "createTo", required = false) Long createTo,
                                                   @RequestParam(value = "articleId", required = false) String articleId,
                                                   @RequestParam(value = "userId", required = false) String userId,
                                                   @RequestParam(value = "sort_by", required = false) String sortBy,
                                                   @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                   @RequestParam(value = "current_page", required = false)
                                                   @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                   @Parameter(description = "Default: 1") Integer currentPage,
                                                   @RequestParam(value = "page_size", required = false)
                                                   @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                   @Max(value = 50, message = "page_size phải bé hơn 50")
                                                   @Parameter(description = "Default: 10") Integer pageSize) {
        log.info("get comments");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchComment searchParam = new ParameterSearchComment();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        if (!Extensions.isBlankOrNull(sortOrder)) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }
        searchParam.setSortField(sortBy);
        searchParam.setId(id);
        searchParam.setContent(content);
        searchParam.setFlag(flag);
        searchParam.setArticleId(articleId);
        searchParam.setUserId(userId);
        if (createFrom != null) {
            searchParam.setCreateFrom(new Date(createFrom));
        }
        if (createTo != null) {
            searchParam.setCreateTo(new Date(createTo));
        }

        return commentService.searchComment(searchParam);
    }

    @Operation(summary = "Lấy comment theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @GetMapping("{id}")
    public CommentProfile getCommentById(@PathVariable("id") String id) {
        ParameterSearchComment searchParam = new ParameterSearchComment();
        searchParam.setId(id);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        ListWrapper<CommentProfile> wrapper = commentService.searchComment(searchParam);
        List<CommentProfile> results = wrapper.getData();
        if (Extensions.isNullOrEmpty(results)) {
            throw new ServiceException("Comment not found");
        }
        return results.get(0);
    }


    @Operation(summary = "Tạo comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @PostMapping
    public CommentProfile createComment(@RequestBody @Valid CreateCommentDto dto) {
        log.info("create comment");
        return commentService.createComment(dto);
    }

    @Operation(summary = "Cập nhật comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @PutMapping
    public CommentProfile updateComment(@RequestBody @Valid UpdateCommentDto dto) {
        log.info("Update comment {}", dto.getId());
        return commentService.updateComment(dto);
    }

    @Operation(summary = "Xoá comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @DeleteMapping("{id}")
    public Object deleteComment(@PathVariable("id") String commentId) {
        log.info("Delete comment {}", commentId);
        commentService.deleteComment(commentId);
        return ObjectResponseWrapper.builder().status(1)
                .data("comment_id " + commentId + " deleted")
                .build();
    }

    @Operation(summary = "Report comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @PostMapping("/reports")
    public CommentProfile reportComment(@RequestBody @Valid ReportCommentDto dto) {
        log.info("Report comment {}", dto.getId());
        return commentService.reportComment(dto);
    }

    @Operation(summary = "Like comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @PostMapping("{id}/likes")
    public CommentProfile likeComment(@PathVariable("id") String commentId) {
        log.info("Like comment {}", commentId);
        return commentService.likeComment(commentId);
    }

    @Operation(summary = "Unlike comment", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = CommentProfile.class)))
    })
    @PutMapping("{id}/unlikes")
    public CommentProfile unlikeComment(@PathVariable("id") String commentId) {
        log.info("Unlike comment {}", commentId);
        return commentService.unlikeComment(commentId);
    }
}
