package com.alsvietnam.service;

import java.io.File;

public interface FileStorageService {

    String uploadFile(File file, String destination);

    Object download(String fileName);

    void deleteFile(String fileName, String destination);
}
