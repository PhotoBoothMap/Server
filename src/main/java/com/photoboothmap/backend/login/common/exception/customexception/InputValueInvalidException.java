package com.photoboothmap.backend.login.common.exception.customexception;


import com.photoboothmap.backend.login.common.exception.CustomException;
import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class InputValueInvalidException extends CustomException {

    private List<ObjectError> objectErrors;

    public InputValueInvalidException(String message, List<ObjectError> objectErrors) {
        super(message);
        this.objectErrors = objectErrors;
    }
}
