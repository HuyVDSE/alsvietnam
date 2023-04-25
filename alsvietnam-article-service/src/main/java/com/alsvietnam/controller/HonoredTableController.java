package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.honoredVolunteer.CreateHonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.HonoredTableDto;
import com.alsvietnam.models.dtos.honoredVolunteer.UpdateHonoredTableDto;
import com.alsvietnam.models.search.ParameterSearchHonoredTable;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;

/**
 * Duc_Huy
 * Date: 11/1/2022
 * Time: 11:35 PM
 */

@RestController
@RequestMapping(Constants.HONORED_TABLE_SERVICE)
@ExtensionMethod(Extensions.class)
@Tag(name = "Honored Table", description = "Honored Table API")
public class HonoredTableController extends BaseController {

    @Operation(summary = "Danh sách bảng vinh danh", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = HonoredTableDto.class))))
    })
    @GetMapping
    public ListWrapper<HonoredTableDto> searchHonoredTable(@RequestParam(value = "id", required = false) String id,
                                                           @RequestParam(value = "ids", required = false)
                                                           @Parameter(description = "List id: id1,id2,id3") String idArray,
                                                           @RequestParam(value = "quarter", required = false) Long quarter,
                                                           @RequestParam(value = "year", required = false) Long year,
                                                           @RequestParam(value = "title", required = false) String title,
                                                           @RequestParam(value = "active", required = false) Boolean active,
                                                           @RequestParam(value = "deleted", required = false) Boolean deleted,
                                                           @RequestParam(value = "build_user", required = false) Boolean buildUser,
                                                           @RequestParam(value = "sort_order", required = false) @Parameter(description = "Allowed values: asc | desc.") String sortOrder,
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
        ParameterSearchHonoredTable searchParameter = new ParameterSearchHonoredTable();
        searchParameter.setId(id);
        if (!idArray.isBlankOrNull()) {
            searchParameter.setIds(Arrays.asList(idArray.split(",")));
        }
        searchParameter.setQuarter(quarter);
        searchParameter.setYear(year);
        searchParameter.setTitle(title);
        searchParameter.setActive(active);
        searchParameter.setDeleted(deleted);
        searchParameter.setStartIndex(startIndex);
        searchParameter.setPageSize(pageSize);
        searchParameter.setBuildUser(buildUser);
        if (!sortOrder.isBlankOrNull()) {
            searchParameter.setDescSort(sortOrder.equals("desc"));
        }
        return honoredTableService.searchHonoredTable(searchParameter);
    }

    @Operation(summary = "Lấy bảng vinh danh theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = HonoredTableDto.class)))
    })
    @GetMapping("{id}")
    public HonoredTableDto getHonoredTableById(@PathVariable("id") String id) {
        ParameterSearchHonoredTable searchParameter = new ParameterSearchHonoredTable();
        searchParameter.setId(id);
        searchParameter.setStartIndex(0L);
        searchParameter.setPageSize(1);
        ListWrapper<HonoredTableDto> wrapper = honoredTableService.searchHonoredTable(searchParameter);
        List<HonoredTableDto> data = wrapper.getData();
        if (data.isNullOrEmpty()) {
            throw new ServiceException("Honored Table not found");
        }
        return data.get(0);
    }

    @Operation(summary = "Tạo bảng vinh danh", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = HonoredTableDto.class)))
    })
    @PostMapping
    public HonoredTableDto createHonoredTable(@RequestBody @Valid CreateHonoredTableDto honoredTableDto) {
        return honoredTableService.create(honoredTableDto);
    }

    @Operation(summary = "Cập nhật bảng vinh danh theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = HonoredTableDto.class)))
    })
    @PutMapping("{id}")
    public HonoredTableDto updateHonoredTable(@PathVariable("id") String id,
                                              @RequestBody @Valid UpdateHonoredTableDto honoredTableDto) {
        return honoredTableService.update(id, honoredTableDto);
    }

    @Operation(summary = "Xoá bảng vinh danh theo Id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = HonoredTableDto.class)))
    })
    @DeleteMapping("{id}")
    public ObjectResponseWrapper deleteHonoredTable(@PathVariable("id") String id) {
        honoredTableService.delete(id);
        return ObjectResponseWrapper.builder().status(1)
                .data("Delete honored table success")
                .build();
    }

    @Operation(summary = "Cập nhật publish bảng vinh danh", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PutMapping("{id}/publish")
    public ObjectResponseWrapper publishHonoredTable(@PathVariable("id") String id) {
        honoredTableService.publishHonor(id);
        return ObjectResponseWrapper.builder().status(1)
                .data("Publish honored table success")
                .build();
    }
}
