package com.photoboothmap.backend.util.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"success", "message", "result"})
public class BaseResponse<T> {

    private Boolean success;
    @JsonInclude(JsonInclude.Include.NON_NULL) // null일때는 출력하지 않도록
    private String message;
    @JsonIgnore
    private int code;
    @JsonInclude(JsonInclude.Include.NON_NULL) // null일때는 출력하지 않도록
    private T result;

    // 성공시
    public BaseResponse(T result) {
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.result = result;
    }

    // 실패시
    public BaseResponse(ResponseStatus status) {
        this.success = false;
        this.message = status.getMessage();
        this.code = status.getCode();
    }


    public ResponseEntity<BaseResponse> convert() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");

        return ResponseEntity.status(this.code).headers(headers).body(this);
    }
}

