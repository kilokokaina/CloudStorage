package com.work.cloudstorage.api;

import com.work.cloudstorage.pojo.FileJson;
import com.work.cloudstorage.service.FileIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping(value = "api")
public class FileAPI {

    private final FileIOService fileIOService;

    @Autowired
    public FileAPI(FileIOService fileIOService) {
        this.fileIOService = fileIOService;
    }

    @RequestMapping(value = "upload/partial", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> upload(@RequestParam(name = "username") String username,
                             @RequestParam(name = "filename") String filename, @RequestBody byte[] byteArray)
            throws IOException, ExecutionException, InterruptedException {
        HttpStatus result = fileIOService.upload(username, filename, byteArray).get();
        return ResponseEntity.status(result).build();
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> download(@RequestParam(value = "username") String username,
            @RequestParam(value = "filename") String filename) throws IOException {
        return fileIOService.download(username, filename);
    }

    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public ResponseEntity<List<FileJson>> tree(@RequestParam(value = "username") String username,
            @RequestParam(value = "filepath") String filepath) {
        return ResponseEntity.ok(fileIOService.tree(username, filepath));
    }

}
