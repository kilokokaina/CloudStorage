package com.work.cloudstorage.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@RestController
public class FileAPI {

    private @Value("${storage.path}") String FILE;
    private static int ERROR_TEST = 0;

    @PostMapping("upload/partial")
    public ResponseEntity<HttpStatus> upload(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "filename") String filename,
            @RequestBody byte[] base64String
    ) throws IOException {
        Path path = Path.of(String.format(FILE, username, filename));

        if (!Files.exists(path)) {
            log.info("File created: " + filename);
            Files.createFile(path);
        }
        Files.write(path, base64String, StandardOpenOption.APPEND);
        log.info(String.format("File '%s' writing", filename));

        ERROR_TEST++;

        HttpStatus httpStatus = (ERROR_TEST  == 3) ?
                HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.ACCEPTED;

        return ResponseEntity.status(httpStatus).build();
    }

    @GetMapping("download")
    public ResponseEntity<FileSystemResource> download(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "filename") String filename) {
        String fileURI = String.format(FILE, username, filename);
        String headerValue = "attachment; filename=%s";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION,
                String.format(headerValue, filename));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new FileSystemResource(fileURI));
    }
}
