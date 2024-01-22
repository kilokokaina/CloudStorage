package com.work.cloudstorage.api;

import com.work.cloudstorage.pojo.FileJson;
import com.work.cloudstorage.service.FileIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping(value = "api/file")
public class FileAPI {

    private final FileIOService fileIOService;

    @Autowired
    public FileAPI(FileIOService fileIOService) {
        this.fileIOService = fileIOService;
    }

    @RequestMapping(value = "upload/partial", method = RequestMethod.POST)
    public ResponseEntity<HttpStatus> upload(Authentication authentication,
                             @RequestParam(name = "filename") String filename, @RequestBody byte[] byteArray)
            throws IOException, ExecutionException, InterruptedException {
        HttpStatus result = fileIOService.upload(authentication.getName(), filename, byteArray).get();
        return ResponseEntity.status(result).build();
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> download(Authentication authentication,
            @RequestParam(value = "filename") String filename) throws IOException {
        return fileIOService.download(authentication.getName(), filename);
    }

    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public ResponseEntity<List<FileJson>> tree(Authentication authentication,
            @RequestParam(value = "filepath") String filepath) {
        return ResponseEntity.ok(fileIOService.tree(authentication.getName(), filepath));
    }

}
