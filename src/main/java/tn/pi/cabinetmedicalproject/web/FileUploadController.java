package tn.pi.cabinetmedicalproject.web; // Mettez le bon package ici

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    // Chemin où les fichiers seront stockés
    private static final String UPLOAD_DIR = "C:/uploads/"; // Changez cela en fonction de votre système

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                // Créer un chemin de fichier
                Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
                // Sauvegarder le fichier
                Files.copy(file.getInputStream(), path);
                return "File uploaded successfully!";
            } else {
                return "No file selected!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "File upload failed!";
        }
    }
}
