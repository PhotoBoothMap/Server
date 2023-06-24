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
}
