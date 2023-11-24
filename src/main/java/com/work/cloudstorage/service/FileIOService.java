package com.work.cloudstorage.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileIOService {

    void upload(String username, String filename, byte[] byteArray) throws IOException;
    ResponseEntity<FileSystemResource> download(String username, String filename) throws IOException;

}
