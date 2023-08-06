package com.photoboothmap.backend.review.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageUtils {
    public static byte[] convertUrlToBinary(String imgUrl){
        try{
            File file = new File(imgUrl);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            return null;
        }
    }
}
