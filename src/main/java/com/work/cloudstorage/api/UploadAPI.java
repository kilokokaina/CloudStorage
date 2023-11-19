package com.work.cloudstorage.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Slf4j
@RestController
@RequestMapping(value = "upload")
public class UploadAPI {

    private @Value("${storage.path}") String FILE;

    @PostMapping("partial")
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

        log.info(String.format("File '%s' writing: ", filename));

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
