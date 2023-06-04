package com.photoboothmap.backend.login.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorException {
    private ErrorCode errorCode;
}
