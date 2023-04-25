package com.alsvietnam.service;

import com.alsvietnam.entities.FileExternal;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface FileService {

    File convertMultipartToFile(MultipartFile multipartFile);

    File convertMultipartToFileWithTimestampName(MultipartFile multipartFile);

    void deleteFileLocal(List<File> files);

    void deleteFileLocal(File file);

    FileExternal uploadFile(MultipartFile multipartFile);

    void deleteFileExternal(String url);
}
