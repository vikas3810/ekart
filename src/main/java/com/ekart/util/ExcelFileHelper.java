package com.ekart.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
public class ExcelFileHelper {

    // method to check that file is of Excel type or not
    public static boolean checkExcelFormat(MultipartFile file) {
        return Objects.equals(file.getContentType(), StringConstants.EXCEL_FORMAT);
    }

    // method to fetch image data in byte[] from file path
    public static byte[] fetchImageDataInByte(String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        return Files.readAllBytes(path);
    }

    // method to get file extension from file name
    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }




}
