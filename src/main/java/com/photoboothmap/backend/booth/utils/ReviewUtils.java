package com.photoboothmap.backend.booth.utils;

import com.photoboothmap.backend.util.entity.TagType;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewUtils {
    public static List<TagType> convertStringToTagEnum(List<String> userTags){
        return userTags.stream()
                .map(
                        tag -> TagType.valueOf(tag)
                ).collect(Collectors.toList());
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
}
