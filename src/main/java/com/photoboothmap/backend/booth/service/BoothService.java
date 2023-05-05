package com.photoboothmap.backend.booth.service;

import com.photoboothmap.backend.booth.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoothService {

    private final BoothRepository boothRepository;

}
