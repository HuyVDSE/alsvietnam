package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.TopOrganizationSupport;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.topOrganizationSupport.CreateTopOrganizationSupportDto;
import com.alsvietnam.models.dtos.topOrganizationSupport.UpdateTopOrganizationSupportDto;
import com.alsvietnam.models.search.ParameterSearchTopOrganizationSupport;
import com.alsvietnam.models.wrapper.ListWrapper;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(Constants.TOP_ORGANIZATION_SUPPORT_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "Top Organization Support", description = "Top Organization Support API")
public class TopOrganizationSupportController extends BaseController {

    @GetMapping
    @Operation(summary = "Danh sách top organization support", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = TopOrganizationSupport.class))))
    })
    public ListWrapper<TopOrganizationSupport> getTopOrganizationSupport(@RequestParam(value = "id", required = false) String id,
                                                                         @RequestParam(value = "organizationName", required = false) String organizationName,
                                                                         @RequestParam(value = "active", required = false) @Parameter(description = "Allowed values: true | false.") Boolean active,
                                                                         @RequestParam(value = "createFrom", required = false) Long createFrom,
                                                                         @RequestParam(value = "createTo", required = false) Long createTo,
                                                                         @RequestParam(value = "deleted", required = false) Boolean deleted,
                                                                         @RequestParam(value = "sort_by", required = false) String sortBy,
                                                                         @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
                                                                         @RequestParam(value = "current_page", required = false)
                                                                         @Min(value = 1, message = "current_page phải lớn hơn 0")
                                                                         @Parameter(description = "Default: 1") Integer currentPage,
                                                                         @RequestParam(value = "page_size", required = false)
                                                                         @Min(value = 1, message = "page_size phải lớn hơn 0")
                                                                         @Max(value = 50, message = "page_size phải bé hơn 50")
                                                                         @Parameter(description = "Default: 10") Integer pageSize) {
        log.info("get top organization support");
        if (currentPage == null || currentPage == 0) {
            currentPage = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        Long startIndex = (long) (currentPage - 1) * pageSize;
        ParameterSearchTopOrganizationSupport searchParam = new ParameterSearchTopOrganizationSupport();
        searchParam.setStartIndex(startIndex);
        searchParam.setPageSize(pageSize);
        if (!Extensions.isBlankOrNull(sortOrder)) {
            searchParam.setDescSort(sortOrder.equals("desc"));
        }
        searchParam.setSortField(sortBy);
        searchParam.setId(id);
        searchParam.setActive(active);
        searchParam.setDeleted(deleted);
        searchParam.setOrganizationName(organizationName);
        if (createFrom != null) {
            searchParam.setCreateFrom(new Date(createFrom));
        }
        if (createTo != null) {
            searchParam.setCreateTo(new Date(createTo));
        }

        return topOrganizationSupportService.searchTopOrganizationSupport(searchParam);
    }

    @Operation(summary = "Lấy top organization theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TopOrganizationSupport.class)))
    })
    @GetMapping("{id}")
    public TopOrganizationSupport getTopOrganizationSupportById(@PathVariable("id") String id) {
        ParameterSearchTopOrganizationSupport searchParam = new ParameterSearchTopOrganizationSupport();
        searchParam.setId(id);
        searchParam.setStartIndex(0L);
        searchParam.setPageSize(1);
        ListWrapper<TopOrganizationSupport> wrapper = topOrganizationSupportService.searchTopOrganizationSupport(searchParam);
        List<TopOrganizationSupport> results = wrapper.getData();
        if (Extensions.isNullOrEmpty(results)) {
            throw new ServiceException("Top organization support not found");
        }
        return results.get(0);
    }

    @Operation(summary = "Tạo top organization support", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TopOrganizationSupport.class)))
    })
    @PostMapping
    public TopOrganizationSupport createTopOrganizationSupport(@RequestBody @Valid CreateTopOrganizationSupportDto dto) {
        return topOrganizationSupportService.createTopOrganizationSupport(dto);
    }


    @Operation(summary = "Cập nhật top organization support", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = TopOrganizationSupport.class)))
    })
    @PutMapping("{id}")
    public TopOrganizationSupport updateTopOrganizationSupport(@PathVariable("id") String id,
                                                               @RequestBody @Valid UpdateTopOrganizationSupportDto dto) {
        dto.setId(id);
        return topOrganizationSupportService.updateTopOrganizationSupport(dto);
    }

    @Operation(summary = "Xoá top organization support", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @DeleteMapping("{id}")
    public ObjectResponseWrapper deleteTopOrganizationSupport(@PathVariable("id") String id) {
        topOrganizationSupportService.disableTopOrganizationSupport(id);
        return ObjectResponseWrapper.builder().status(1).
                message("Delete top organization support success").build();
    }
}
