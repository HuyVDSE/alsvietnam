package com.alsvietnam.controller;

import com.alsvietnam.controller.base.BaseController;
import com.alsvietnam.entities.Topic;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.models.dtos.topic.CreateTopicDto;
import com.alsvietnam.models.wrapper.ObjectResponseWrapper;
import com.alsvietnam.utils.Constants;
import com.alsvietnam.utils.Extensions;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(Constants.EXCEL_SERVICE)
@ExtensionMethod(Extensions.class)
@Slf4j
@Tag(name = "Excel", description = "Excel API")
public class ExcelController extends BaseController {

    @SneakyThrows
    @PostMapping("/topics")
    public ObjectResponseWrapper importExcelTopic(@RequestParam("file") MultipartFile topicExcel) {
        log.info("Import Topic excel");
        XSSFWorkbook workbook = new XSSFWorkbook(topicExcel.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String, String> topicParentMap = new HashMap<>();
        // row index 0 will contain header
        int totalRows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < totalRows; i++) {
            XSSFRow row = sheet.getRow(i);
            String topicParentName = row.getCell(2) == null ? null : row.getCell(2).getStringCellValue();
            String topicParentId = findTopicParentId(topicParentMap, topicParentName);
            CreateTopicDto dto = CreateTopicDto.builder()
                    .titleEnglish(row.getCell(0).getStringCellValue())
                    .titleVietnamese(row.getCell(1).getStringCellValue())
                    .topicParentId(topicParentId)
                    .build();
            try {
                topicService.createTopic(dto);
            } catch (ServiceException e) {
                log.info(e.getMessage());
            }

            log.info("index: {}/{}", i, totalRows - 1);
        }

        return ObjectResponseWrapper.builder()
                .status(1)
                .data("Import topic thành công")
                .build();
    }

    @SneakyThrows
    @PostMapping("/articles")
    public ObjectResponseWrapper importExcelArticle(@RequestParam("file") MultipartFile articleExcel) {
        log.info("Import Article excel");
        XSSFWorkbook workbook = new XSSFWorkbook(articleExcel.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        Map<String, String> topicParentMap = new HashMap<>();
        // row index 0 will contain header
        int totalRows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < totalRows; i++) {
            XSSFRow row = sheet.getRow(i);
            String topicParentName = row.getCell(6) == null ? null : row.getCell(6).getStringCellValue();
            if (topicParentName.isBlankOrNull()) {
                continue;
            }
            String topicParentId = findTopicParentId(topicParentMap, topicParentName);
//            CreateArticleDto dto = CreateArticleDto.builder()
//                    .title(row.getCell(0).getStringCellValue())
//                    .description(row.getCell(1).getStringCellValue())
//                    .content(row.getCell(2)== null ? null : row.getCell(2).getStringCellValue())
//                    .author(row.getCell(3) == null ? null : row.getCell(3).getStringCellValue())
//                    .label(row.getCell(4) == null ? null : row.getCell(4).getStringCellValue())
//                    .languageType(row.getCell(5) == null ? null : row.getCell(5).getStringCellValue())
//                    .topicId(topicParentId)
//                    .build();
//            articleService.createArticle(dto);
            log.info("index: {}/{}", i, totalRows - 1);
        }

        return ObjectResponseWrapper.builder()
                .status(1)
                .data("Import article thành công")
                .build();
    }

    private String findTopicParentId(Map<String, String> topicParentMap, String topicParentName) {
        if (topicParentName == null) {
            return null;
        }
        String topicParentId = topicParentMap.get(topicParentName);
        if (topicParentId != null && topicParentId.equals("empty")) {
            topicParentId = null;
        } else {
            Optional<Topic> topicParentOptional = topicRepository.findByTitleEnglish(topicParentName);
            if (topicParentOptional.isPresent()) {
                Topic topicParent = topicParentOptional.get();
                topicParentId = topicParent.getId();
                topicParentMap.put(topicParent.getTitleEnglish(), topicParent.getId());
            } else {
                topicParentMap.put(topicParentName, "empty");
            }
        }
        return topicParentId;
    }
}
