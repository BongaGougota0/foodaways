package za.co.foodaways.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static String writeImage(MultipartFile productImage) {
        String uploadDirectory = "src/main/resources/static/assets/images";
        String fileName = null;
        if (!productImage.isEmpty()) {
            fileName = productImage.getOriginalFilename();
            try {
                File newDir = new File(uploadDirectory);
                if (!newDir.exists()) {
                    newDir.mkdirs();
                }
                byte[] fileBytes = productImage.getBytes();
                Path path = Paths.get(uploadDirectory, fileName);
                Files.write(path, fileBytes);
                return fileName;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (productImage.isEmpty()) {
            throw new RuntimeException("Could not write image");
        };
        throw new RuntimeException("Could not write image");
    }

}
