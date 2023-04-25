package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.article.*;
import com.alsvietnam.models.dtos.article.content.CreateArticleContentRequest;
import com.alsvietnam.models.dtos.article.content.UpdateArticleContentRequest;
import com.alsvietnam.models.dtos.article.file.ArticleFileDto;
import com.alsvietnam.models.dtos.article.file.CreateArticleFileRequest;
import com.alsvietnam.models.dtos.article.file.UpdateArticleFileRequest;
import com.alsvietnam.models.dtos.article.media.ArticleMediaDto;
import com.alsvietnam.models.dtos.article.media.CreateArticleMediaRequest;
import com.alsvietnam.models.dtos.article.media.UpdateArticleMediaRequest;
import com.alsvietnam.models.profiles.ArticleProfile;
import com.alsvietnam.models.search.ParameterSearchArticle;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.models.wrapper.StringResponseWrapper;
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
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Constants.ARTICLE_SERVICE)
@ExtensionMethod(Extensions.class)
@Tag(name = "Article", description = "Article API")
@Slf4j
public class ArticleController extends BaseController {

    @Operation(summary = "Danh sách article", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ArticleProfile.class))))
    })
    @GetMapping
    public ListWrapper<ArticleProfile> getArticles(@RequestParam(value = "id", required = false) String id,
                                                   @RequestParam(value = "author", required = false) String author,
                                                   @RequestParam(value = "label", required = false) String label,
                                                   @RequestParam(value = "status", required = false) String status,
                                                   @RequestParam(value = "title", required = false) String title,
                                                   @RequestParam(value = "translator", required = false) String translator,
                                                   @RequestParam(value = "topic_id", required = false) String topicId,
                                                   @RequestParam(value = "create_from", required = false) Long createFrom,
                                                   @RequestParam(value = "create_to", required = false) Long createTo,
                                                   @RequestParam(value = "created_user", required = false) String createdUser,
                                                   @RequestParam(value = "sort_by", required = false) String sortBy,
                                                   @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                   @RequestParam(value = "current_page", required = false)
                                                   @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                   @Parameter(description = "Default: 1") Integer currentPage,
                                                   @RequestParam(value = "page_size", required = false)
                                                   @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                   @Max(value = 50, message = "page_size phải bé hơn 50")
                                                   @Parameter(description = "Default: 10") Integer pageSize) {
        log.info("get articles");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchArticle searchParam = new ParameterSearchArticle();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        if (!sortOrder.isBlankOrNull()) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }
        searchParam.setSortField(sortBy);
        searchParam.setId(id);
        searchParam.setLabel(label);
        searchParam.setAuthor(author);
        searchParam.setTranslator(translator);
        searchParam.setStatus(status);
        searchParam.setTitle(title);
        searchParam.setTopicId(topicId);
        searchParam.setCreatedUser(createdUser);
        searchParam.setLikeArticle(false);
        if (createFrom != null) {
            searchParam.setCreateFrom(new Date(createFrom));
        }
        if (createTo != null) {
            searchParam.setCreateTo(new Date(createTo));
        }
        return articleService.searchArticle(searchParam);
    }

    @Operation(summary = "Lấy article theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @GetMapping("{id}")
    public ArticleProfile getArticleById(@PathVariable("id") String id) {
        ParameterSearchArticle searchParam = new ParameterSearchArticle();
        searchParam.setId(id);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        searchParam.setLikeArticle(true);
        ListWrapper<ArticleProfile> wrapper = articleService.searchArticle(searchParam);
        List<ArticleProfile> results = wrapper.getData();
        if (results.isNullOrEmpty()) {
            throw new ServiceException("Article not found");
        }
        return results.get(0);
    }

    @Operation(summary = "Tạo article", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleProfile createArticle(@ModelAttribute @Valid CreateArticleDto articleDto,
                                        BindingResult bindingResult) throws BindException {
        log.info("create article");
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return articleService.createArticle(articleDto);
    }

    @Operation(summary = "Tạo article contents", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PostMapping(value = "{id}/contents")
    public ArticleProfile createArticleContent(@PathVariable("id") String articleId,
                                               @RequestBody @Valid CreateArticleContentRequest contentModel) {
        log.info("create article content");
        contentModel.setArticleId(articleId);
        return articleService.createArticleContent(contentModel);
    }

    @Operation(summary = "Tạo article files", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleFileDto.class)))
    })
    @PostMapping(value = "{id}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleFileDto createArticleFile(@PathVariable("id") String articleId,
                                            @ModelAttribute @Valid CreateArticleFileRequest fileDto,
                                            BindingResult bindingResult) throws BindException {
        log.info("create article file");
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return articleService.createArticleFile(fileDto, articleId);
    }

    @Operation(summary = "Tạo article medias", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleMediaDto.class)))
    })
    @PostMapping(value = "{id}/medias", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleMediaDto createArticleMedia(@PathVariable("id") String articleId,
                                              @ModelAttribute @Valid CreateArticleMediaRequest mediaDto,
                                              BindingResult bindingResult) throws BindException {
        log.info("create article media");
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return articleService.createArticleMedia(mediaDto, articleId);
    }

    @Operation(summary = "Cập nhật article", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleProfile updateArticle(@ModelAttribute @Valid UpdateArticleDto articleDto,
                                        BindingResult bindingResult) throws BindException {
        log.info("update article {}", articleDto.getId());
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return articleService.updateArticle(articleDto);
    }

    @Operation(summary = "Cập nhật chỉ thông tin của article", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @SneakyThrows
    @PutMapping(value = "{id}")
    public ArticleProfile updateArticle(@PathVariable("id") String articleId,
                                        @RequestBody @Valid UpdateBaseArticleDto articleDto) {
        log.info("update article {}", articleId);
        UpdateBaseArticleCustom updateBaseArticleCustom = new UpdateBaseArticleCustom(articleId, articleDto);
        return articleService.updateArticle(updateBaseArticleCustom);
    }

    @Operation(summary = "Cập nhật article content", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PutMapping("{id}/contents")
    public ArticleProfile updateArticleContent(@PathVariable("id") String articleId,
                                               @RequestBody @Valid UpdateArticleContentRequest contentModel) {
        log.info("update article content");
        contentModel.setArticleId(articleId);
        return articleService.updateArticleContent(contentModel);
    }

    @Operation(summary = "Cập nhật article file", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PutMapping(value = "/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleProfile updateArticleFile(@ModelAttribute @Valid UpdateArticleFileRequest file,
                                            BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        log.info("update article file {}", file.getId());
        return articleService.updateArticleFile(file);
    }

    @Operation(summary = "Cập nhật article media", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PutMapping(value = "/medias", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArticleProfile updateArticleMedia(@ModelAttribute @Valid UpdateArticleMediaRequest medias,
                                             BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        log.info("update article media {}", medias.getArticleId());
        return articleService.updateArticleMedia(medias);
    }

    @Operation(summary = "Cập nhật article status", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleProfile.class)))
    })
    @PutMapping(value = "{id}/status/{status}")
    public ArticleProfile updateArticleStatus(@PathVariable("id") String articleId,
                                              @PathVariable("status") String status) {
        log.info("update article status");
        return articleService.updateArticleStatus(articleId, status);
    }

    @Operation(summary = "Xoá article")
    @DeleteMapping("{id}")
    public ObjectResponseWrapper deleteArticle(@PathVariable String id) {
        log.info("delete article {}", id);
        articleService.deleteArticle(id);
        return ObjectResponseWrapper.builder().status(1)
                .message("Delete article successfully")
                .build();
    }

    @Operation(summary = "Xoá article content")
    @DeleteMapping("{id}/content/{content_id}")
    public ObjectResponseWrapper deleteArticleContent(@PathVariable("id") String id, @PathVariable("content_id") String contentId) {
        log.info("delete article content {}", contentId);
        articleService.deleteArticleContent(id, contentId);
        return ObjectResponseWrapper.builder().status(1)
                .message("Delete article content successfully")
                .build();
    }

    @Operation(summary = "Xoá article file")
    @DeleteMapping("{id}/file/{file_id}")
    public ObjectResponseWrapper deleteArticleFile(@PathVariable("id") String id, @PathVariable("file_id") String fileId) {
        log.info("delete article file {}", fileId);
        articleService.deleteArticleFile(id, fileId);
        return ObjectResponseWrapper.builder().status(1)
                .message("Delete article file successfully")
                .build();
    }

    @Operation(summary = "Xoá article media")
    @DeleteMapping("{id}/media/{media_id}")
    public ObjectResponseWrapper deleteArticleMedia(@PathVariable("id") String id, @PathVariable("media_id") String mediaId) {
        log.info("delete article media {}", mediaId);
        articleService.deleteArticleMedia(id, mediaId);
        return ObjectResponseWrapper.builder().status(1)
                .message("Delete article media successfully")
                .build();
    }

    @Operation(summary = "Like article", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = StringResponseWrapper.class)))
    })
    @PostMapping("{id}/likes")
    public StringResponseWrapper likeArticle(@PathVariable("id") String articleId) {
        log.info("Like article {}", articleId);
        articleService.likeArticle(articleId);
        return StringResponseWrapper.builder().status(1)
                .message("like article success").build();
    }

    @Operation(summary = "Unlike article", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = StringResponseWrapper.class)))
    })
    @PutMapping("{id}/unlikes")
    public StringResponseWrapper unlikeArticle(@PathVariable("id") String articleId) {
        log.info("Unlike article {}", articleId);
        articleService.unlikeArticle(articleId);
        return StringResponseWrapper.builder().status(1)
                .message("unlike article success").build();
    }

    @Operation(summary = "Get like article", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ArticleLikeDto.class)))
    })
    @GetMapping("{id}/likes")
    public ArticleLikeDto getLikeArticle(@PathVariable("id") String articleId) {
        log.info("Get Like comment {}", articleId);
        return articleService.getLikeArticle(articleId);
    }
}
