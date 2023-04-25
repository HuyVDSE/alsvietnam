package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.FileExternal;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.models.wrapper.StringResponseWrapper;
import com.alsvietnam.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Duc_Huy
 * Date: 10/4/2022
 * Time: 10:12 PM
 */

@RestController
@RequestMapping(Constants.FILE_SERVICE)
@Tag(name = "File", description = "File API")
public class FileController extends BaseController {

    @Operation(summary = "Upload File", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ObjectResponseWrapper uploadFileExternal(@ModelAttribute MultipartFile multipartFile) {
        FileExternal fileExternal = fileService.uploadFile(multipartFile);
        return ObjectResponseWrapper.builder().status(1).data(fileExternal.getUrl()).build();
    }

    @Operation(summary = "Delete File", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = ObjectResponseWrapper.class)))
    })
    @DeleteMapping
    public StringResponseWrapper deleteFileExternal(@RequestParam("url") String url) {
        fileService.deleteFileExternal(url);
        return StringResponseWrapper.builder().status(1).message("delete file success").build();
    }
}