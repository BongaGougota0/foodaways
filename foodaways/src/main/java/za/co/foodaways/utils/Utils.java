package za.co.foodaways.utils;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    private static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static String writeImage(MultipartFile productImage) {
        String uploadDirectory = "src/main/resources/static/assets/images";
        String fileName = null;
        if (!productImage.isEmpty()) {
            fileName = productImage.getOriginalFilename();
//            String nameExtension = fileName.split(".")[1];
            fileName = getRandomName(10, ".png");
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

    public static String getRandomName(int newNameLength, String extension){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < newNameLength; i++){
            int indexer = (int)(newNameLength*Math.random());
            char character = CHARACTERS.charAt(indexer);
            sb.append(character);
        }
        return  sb.append(extension).toString();
    }

}
