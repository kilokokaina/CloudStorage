package com.work.cloudstorage.api;

import com.work.cloudstorage.service.FileIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class FileAPI {

    private final FileIOService fileIOService;

    @Autowired
    public FileAPI(FileIOService fileIOService) {
        this.fileIOService = fileIOService;
    }

    @PostMapping("upload/partial")
    public ResponseEntity<HttpStatus> upload(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "filename") String filename,
            @RequestBody byte[] byteArray) throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<HttpStatus>> resultStatus = fileIOService.upload(username, filename, byteArray);
        return resultStatus.get();
    }

    @GetMapping("download")
    public ResponseEntity<FileSystemResource> download(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "filename") String filename) throws IOException {
        return fileIOService.download(username, filename);
    }

}
