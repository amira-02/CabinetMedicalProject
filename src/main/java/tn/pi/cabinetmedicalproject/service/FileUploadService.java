package tn.pi.cabinetmedicalproject.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;

public class FileUploadService {

    private final String uploadDir = "src/main/resources/static/images/";

    public void saveFile(InputStream inputStream, String fileName) throws IOException {
        // Define the path where the file will be stored
        Path path = Paths.get(uploadDir + fileName);

        // Use Files.copy to save the file
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }
}
