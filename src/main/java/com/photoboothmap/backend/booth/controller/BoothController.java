package com.photoboothmap.backend.booth.controller;

import com.photoboothmap.backend.booth.service.BoothService;
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
    @ResponseBody
    @GetMapping("/map")
    public ResponseEntity<BaseResponse> getBoothMap(
            @RequestParam Double curx,
            @RequestParam Double cury,
            @RequestParam Double nex,
            @RequestParam Double ney) {
        Map<String, Object> boothList = boothService.getBoothMap(curx, cury, nex, ney);
        return new BaseResponse<>(boothList).convert();
    }

}
