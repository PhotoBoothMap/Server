package com.photoboothmap.backend.booth.controller;

import com.photoboothmap.backend.booth.service.BoothService;
import com.photoboothmap.backend.util.config.BaseException;
import com.photoboothmap.backend.util.config.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

    @ResponseBody
    @GetMapping("/map")
    public ResponseEntity<BaseResponse> getBoothMap(
            @RequestParam Double curx,
            @RequestParam Double cury,
            @RequestParam Double nex,
            @RequestParam Double ney,
            @RequestParam String filter) {
        try {
            Map<String, Object> boothList = boothService.getBoothMap(curx, cury, nex, ney, filter);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @GetMapping("/map/list")
    public ResponseEntity<BaseResponse> getBoothList(
            @RequestParam Double curx,
            @RequestParam Double cury,
            @RequestParam int count,
            @RequestParam String filter) {
        try {
            Map<String, Object> boothList = boothService.getBoothList(curx, cury, count, filter);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

    @ResponseBody
    @GetMapping("/map/search")
    public ResponseEntity<BaseResponse> getBoothSearch(
            @RequestParam Double curx,
            @RequestParam Double cury,
            @RequestParam Double nex,
            @RequestParam Double ney,
            @RequestParam String keyword) {
        try {
            Map<String, Object> boothList = boothService.getBoothSearch(curx, cury, nex, ney, keyword);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

}
