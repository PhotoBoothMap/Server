package com.photoboothmap.backend.login.common.exception.customexception;


import com.photoboothmap.backend.login.common.exception.CustomException;

public class TokenInvalidException extends CustomException {
    public TokenInvalidException(String message) {
        super(message);
    }
}
