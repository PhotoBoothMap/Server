package com.photoboothmap.backend.util.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends Exception {
    private ResponseStatus status;
}
