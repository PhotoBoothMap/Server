package com.photoboothmap.backend.booth.controller;

import com.photoboothmap.backend.booth.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoothController {

    private final BoothService boothService;

}
