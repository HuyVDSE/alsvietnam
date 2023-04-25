package com.alsvietnam.service.impl;

import com.alsvietnam.properties.GoogleFirebaseProperties;
import com.alsvietnam.service.FileStorageService;
import com.alsvietnam.service.base.BaseService;
import com.alsvietnam.utils.Extensions;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@ExtensionMethod(value = Extensions.class)
public class FileStorageServiceImpl extends BaseService implements FileStorageService {

    private final GoogleFirebaseProperties googleFirebaseProperties;

    private final FirebaseApp firebaseApp;

    @Value("${location.upload-folder}")
    private String uploadFolder;

    @Override
    @SneakyThrows
    public String uploadFile(File file, String destination) {
        //init firebase app
        StorageClient storageClient = StorageClient.getInstance(firebaseApp);

        Tika tika = new Tika();

        BlobId blobId = BlobId.of(googleFirebaseProperties.getBucket(), destination + "/" + file.getName());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(Extensions.getType(tika.detect(file)))
                .build();

        Storage storage = storageClient.bucket().getStorage();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format(googleFirebaseProperties.getUrl(), destination + "/" + file.getName());
    }

    @Override
    @SneakyThrows
    public Object download(String fileName) {
        String destFileName = UUID.randomUUID().toString().concat(Extensions.getExtension(fileName));
        String destFilePath = uploadFolder + "/" + destFileName;

        Path path = Paths.get("");
        log.info(path.toAbsolutePath().toString());
        String currentPath = path.toAbsolutePath() + "\\alsvietnam-article-service\\";
        Credentials credentials = GoogleCredentials.fromStream(Files.newInputStream(
                Paths.get(currentPath + googleFirebaseProperties.getPrivateKey())));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(googleFirebaseProperties.getBucket(), fileName));
        blob.downloadTo(Paths.get(destFilePath));

        return "Download Successful";
    }

    @Override
    public void deleteFile(String fileName, String destination) {
        StorageClient storageClient = StorageClient.getInstance(firebaseApp);

        BlobId blobId = BlobId.of(googleFirebaseProperties.getBucket(), destination + "/" + fileName);

        Storage storage = storageClient.bucket().getStorage();
        storage.delete(blobId);
    }
}
