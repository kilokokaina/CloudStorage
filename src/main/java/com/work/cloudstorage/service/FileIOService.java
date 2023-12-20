package com.work.cloudstorage.service;

import com.work.cloudstorage.pojo.FileJson;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FileIOService {

    CompletableFuture<HttpStatus> upload(String username, String filename, byte[] byteArray) throws IOException;
    ResponseEntity<FileSystemResource> download(String username, String filename) throws IOException;
    List<FileJson> tree(String username, String filepath);

}
