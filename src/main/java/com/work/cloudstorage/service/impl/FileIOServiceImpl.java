package com.work.cloudstorage.service.impl;

import com.work.cloudstorage.service.FileIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;

@Slf4j
@Service
public class FileIOServiceImpl implements FileIOService {

    private SoftReference<Path> pathRef = new SoftReference<>(null);
    private @Value("${storage.path}") String FILE;

    @Async
    @Override
    public  void upload(String username, String filename, byte[] byteArray) throws IOException {
        filename = Normalizer.normalize(filename, Normalizer.Form.NFC);
        filename = filename.replace(" ", "_");
        Path path = pathRef.get();

        if (path == null || !path.toString().equals(String.format(FILE, username, filename))) {
            path = Path.of(String.format(FILE, username, filename));
            pathRef = new SoftReference<>(path);
        }

        if (!Files.exists(path)) Files.createFile(path);
        Files.write(path, byteArray, StandardOpenOption.APPEND);

        log.info("Writing in file: " + filename);
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
                new String(filename.getBytes(StandardCharsets.US_ASCII))));
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.toFile().length()));
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, String.valueOf(mediaType));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(new FileSystemResource(fileURI));
    }

}
