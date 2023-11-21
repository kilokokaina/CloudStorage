package com.work.cloudstorage.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface FileIOService {

    CompletableFuture<ResponseEntity<HttpStatus>> upload(String username, String filename, byte[] byteArray) throws IOException;
    ResponseEntity<FileSystemResource> download(String username, String filename) throws IOException;

}
