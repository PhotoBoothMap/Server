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
        Map<String, Object> boothList = boothService.getBoothMap(curx, cury, nex, ney, filter);
        return new BaseResponse<>(boothList).convert();
    }

    @ResponseBody
    @GetMapping("/map/list")
    public ResponseEntity<BaseResponse> getBoothList(
            @RequestParam Double curx,
            @RequestParam Double cury,
            @RequestParam int count) {
        System.out.println("curx(lng) = " + curx);
        System.out.println("cury(lat) = " + cury);
        try {
            Map<String, Object> boothList = boothService.getBoothList(curx, cury, count);
            return new BaseResponse<>(boothList).convert();
        } catch (BaseException ex) {
            return new BaseResponse<>(ex.getStatus()).convert();
        }
    }

}
