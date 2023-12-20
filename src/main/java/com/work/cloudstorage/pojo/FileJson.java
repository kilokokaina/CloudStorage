package com.work.cloudstorage.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileJson {
    private FileType fileType;
    private String filePath;
    private String fileName;
}
