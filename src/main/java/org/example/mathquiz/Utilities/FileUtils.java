package org.example.mathquiz.Utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.io.IOException;

public class FileUtils {
    public static final String FOlDER_MEDIA = "src/main/resources/static/images/user/";

    public static String saveFile(MultipartFile multipartFile) throws IOException {
        File folder = new File(FOlDER_MEDIA);
        if (!folder.exists()) folder.mkdirs();
        InputStream initialStream = multipartFile.getInputStream();
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        String fileName = System.currentTimeMillis() + multipartFile.getOriginalFilename();
        File targetFile = new File(FOlDER_MEDIA + fileName);

        try (OutputStream outStream = new FileOutputStream(targetFile)) {
            outStream.write(buffer);
        }
        return fileName;
    }
}