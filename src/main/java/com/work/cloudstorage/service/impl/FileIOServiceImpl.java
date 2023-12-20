package com.work.cloudstorage.service.impl;

import com.work.cloudstorage.pojo.FileJson;
import com.work.cloudstorage.pojo.FileType;
import com.work.cloudstorage.service.FileIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class FileIOServiceImpl implements FileIOService {

    private SoftReference<Path> pathRef = new SoftReference<>(null);
    private final TranslitService translitService;
    private @Value("${storage.path}") String FILE;

    @Autowired
    public FileIOServiceImpl(TranslitService translitService) {
        this.translitService = translitService;
    }

    @Async
    @Override
    public CompletableFuture<HttpStatus> upload(String username, String filename, byte[] byteArray)
            throws IOException {
        Path path = pathRef.get();

        filename = Normalizer.normalize(filename, Normalizer.Form.NFC);
        filename = filename.replace(" ", "_");

        if (path == null || !path.toString().equals(String.format(FILE, username, filename))) {
            path = Path.of(String.format(FILE, username, filename));
            pathRef = new SoftReference<>(path);
        }

        if (!Files.exists(path)) Files.createFile(path);
        Files.write(path, byteArray, StandardOpenOption.APPEND);

        log.info("Writing in file: " + filename);

        return CompletableFuture.completedFuture(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<FileSystemResource> download(String username, String filename) throws IOException {
        filename = Normalizer.normalize(filename, Normalizer.Form.NFC);
        filename = filename.replace(" ", "_");

        String fileURI = String.format(FILE, username, filename);
        String headerValue = "attachment; filename=\"%s\"";

        Path file = Path.of(fileURI);

        String[] contentType = Files.probeContentType(file).split("/");
        MediaType mediaType = new MediaType(contentType[0], contentType[1]);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, String.format(headerValue,
                translitService.translitCyrillic(filename)));
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.toFile().length()));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, String.valueOf(mediaType));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new FileSystemResource(fileURI));
    }

    @Override
    public List<FileJson> tree(String username, String filepath) {
        File[] files = new File(String.format(FILE, username, filepath)).listFiles();
        List<FileJson> result = new ArrayList<>();

        assert files != null;
        for (File file : files) {
            if (!file.isHidden()) {
                result.add(new FileJson(
                        (file.isDirectory()) ? FileType.DIR : FileType.FILE,
                        file.getAbsolutePath(),
                        file.getName()
                ));
            }
        }

        return result;
    }

}

