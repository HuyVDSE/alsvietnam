package com.alsvietnam.service.impl;

import com.alsvietnam.entities.FileExternal;
import com.alsvietnam.handler.ServiceException;
import com.alsvietnam.service.FileService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.DateUtil;
import com.alsvietnam.utils.EncryptUtil;
import com.alsvietnam.utils.Extensions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
@ExtensionMethod(value = Extensions.class)
public class FileServiceImpl extends BaseService implements FileService {

    @Value("${location.upload-folder}")
    private String uploadFolder;

    private final Tika tika;

    @SneakyThrows
    public void deleteFileLocal(List<File> files) {
        for (File file : files) {
            Files.delete(file.toPath());
        }
    }

    @SneakyThrows
    public void deleteFileLocal(File file) {
        Files.delete(file.toPath());
    }

    @SneakyThrows
    @Override
    public FileExternal uploadFile(MultipartFile multipartFile) {
        File file = convertMultipartToFile(multipartFile);
        String url = fileStorageService.uploadFile(file, "general");
        String mime = tika.detect(file);

        FileExternal fileExternal = FileExternal.builder()
                .name(file.getName())
                .url(url)
                .mime(mime)
                .type(mime.getType())
                .extension(file.getName().getExtension())
                .createdAt(new Date())
                .createdBy(userService.getUsernameLogin())
                .build();
        fileExternalRepository.save(fileExternal);

        deleteFileLocal(file);
        return fileExternal;
    }

    @Override
    public void deleteFileExternal(String url) {
        if (url.isBlankOrNull()) {
            throw new ServiceException("URL is empty");
        }
        FileExternal fileExternal = fileExternalRepository.findByUrl(url);
        if (fileExternal == null) {
            throw new ServiceException("File External not found");
        }
        fileStorageService.deleteFile(fileExternal.getName(), "general");
        fileExternalRepository.delete(fileExternal);
    }

    @Override
    public File convertMultipartToFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        fileName = UUID.randomUUID().toString().concat(Extensions.getExtension(Objects.requireNonNull(fileName)));
        return this.convertToFile(multipartFile, fileName);
    }

    @Override
    public File convertMultipartToFileWithTimestampName(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String extension = Extensions.getExtension(Objects.requireNonNull(fileName));
        Date date = new Date();
        fileName = fileName.replace(extension, "");
        fileName = fileName.concat("-" + DateUtil.convertDateToString(date, "yyyyMMdd'T'HHmmss") + extension);
        fileName = fileName.replace(" ", "_");
        fileName = EncryptUtil.unicode2NoSign(fileName);
        if (!Pattern.matches("^[a-zA-Z\\d_.\\-()]+$", fileName)) {
            throw new ServiceException("File name contain character, number, round brackets, underscore, dot, hyphen only");
        }
        return this.convertToFile(multipartFile, fileName);
    }

    @SneakyThrows
    private File convertToFile(MultipartFile multipartFile, String fileName) {
        File tempFile = new File(uploadFolder, fileName);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }
}
