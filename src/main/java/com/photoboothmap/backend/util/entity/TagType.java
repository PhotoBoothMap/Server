package com.photoboothmap.backend.util.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TagType {

    PICTURE("사진","사진이 잘 나와요"),
    LIGHT("사진","조명이 좋아요"),
    RETOUCH("사진","보정이 자연스러워요"),
    VARIOUS("소품/부스","소품이 다양해요"),
    CLEAN("소품/부스","소품이 깨끗해요"),
    BOOTH("소품/부스","부스 구성이 다양해요"),
    FACILITY("사진","시설이 깔끔해요"),
    POWDER_ROOM("시설","파우더룸이 잘 되어있어요"),
    PARKING("시설","주차가 편해요");

    private String category;
    private String tag;

}
