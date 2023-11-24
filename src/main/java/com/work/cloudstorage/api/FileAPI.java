package com.work.cloudstorage.api;

import com.work.cloudstorage.service.FileIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
public class FileAPI {

    private final FileIOService fileIOService;

    @Autowired
    public FileAPI(FileIOService fileIOService) {
        this.fileIOService = fileIOService;
    }

    @PostMapping("upload/partial")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void upload(@RequestParam(name = "username") String username,
            @RequestParam(name = "filename") String filename, @RequestBody byte[] byteArray) throws IOException {
        fileIOService.upload(username, filename, byteArray);
    }

    @GetMapping("download")
    public ResponseEntity<FileSystemResource> download(@RequestParam(value = "username") String username,
            @RequestParam(value = "filename") String filename) throws IOException {
        return fileIOService.download(username, filename);
    }

}
