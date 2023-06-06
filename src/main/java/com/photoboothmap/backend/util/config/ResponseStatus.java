package com.photoboothmap.backend.util.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseStatus {

    // 에러 메세지 커스텀. 코드는 추후 번호 제한
    // 코드는 중복되어도 상관 x, 대신 이름은 유니크한 값
    TEST_STATUS( false, 452, "Custom message example"),
    WRONG_LATLNG_RANGE(false, 453, "wrong latitude/longitude range"),
    WRONG_BRAND_NAME(false, 453, "wrong brand name"),
    EMPTY_KEYWORD(false, 453, "empty keyword"),


    // 아래는 false로 들어가는 기본 HttpStatus, deprecated 제외

    // --- 4xx Client Error ---
    BAD_REQUEST(400, HttpStatus.Series.CLIENT_ERROR, "Bad Request"),
    UNAUTHORIZED(401,HttpStatus.Series.CLIENT_ERROR, "Unauthorized"),
    PAYMENT_REQUIRED(402,HttpStatus.Series.CLIENT_ERROR, "Payment Required"),
    FORBIDDEN(403,HttpStatus.Series.CLIENT_ERROR, "Forbidden"),
    NOT_FOUND(404,HttpStatus.Series.CLIENT_ERROR, "Not Found"),
    METHOD_NOT_ALLOWED(405,HttpStatus.Series.CLIENT_ERROR, "Method Not Allowed"),
    NOT_ACCEPTABLE(406,HttpStatus.Series.CLIENT_ERROR, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407,HttpStatus.Series.CLIENT_ERROR, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408,HttpStatus.Series.CLIENT_ERROR, "Request Timeout"),
    CONFLICT(409,HttpStatus.Series.CLIENT_ERROR, "Conflict"),
    GONE(410,HttpStatus.Series.CLIENT_ERROR, "Gone"),
    LENGTH_REQUIRED(411,HttpStatus.Series.CLIENT_ERROR, "Length Required"),
    PRECONDITION_FAILED(412,HttpStatus.Series.CLIENT_ERROR, "Precondition Failed"),
    PAYLOAD_TOO_LARGE(413,HttpStatus.Series.CLIENT_ERROR, "Payload Too Large"),
    URI_TOO_LONG(414,HttpStatus.Series.CLIENT_ERROR, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415,HttpStatus.Series.CLIENT_ERROR, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416,HttpStatus.Series.CLIENT_ERROR, "Requested range not satisfiable"),
    EXPECTATION_FAILED(417,HttpStatus.Series.CLIENT_ERROR, "Expectation Failed"),
    I_AM_A_TEAPOT(418,HttpStatus.Series.CLIENT_ERROR, "I'm a teapot"),
    DESTINATION_LOCKED(421,HttpStatus.Series.CLIENT_ERROR, "Destination Locked"),
    UNPROCESSABLE_ENTITY(422,HttpStatus.Series.CLIENT_ERROR, "Unprocessable Entity"),
    LOCKED(423,HttpStatus.Series.CLIENT_ERROR, "Locked"),
    FAILED_DEPENDENCY(424,HttpStatus.Series.CLIENT_ERROR, "Failed Dependency"),
    TOO_EARLY(425,HttpStatus.Series.CLIENT_ERROR, "Too Early"),
    UPGRADE_REQUIRED(426,HttpStatus.Series.CLIENT_ERROR, "Upgrade Required"),
    PRECONDITION_REQUIRED(428,HttpStatus.Series.CLIENT_ERROR, "Precondition Required"),
    TOO_MANY_REQUESTS(429,HttpStatus.Series.CLIENT_ERROR, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE(431,HttpStatus.Series.CLIENT_ERROR, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS(451,HttpStatus.Series.CLIENT_ERROR, "Unavailable For Legal Reasons"),

    // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR(500,HttpStatus.Series.SERVER_ERROR, "Internal Server Error"),
    NOT_IMPLEMENTED(501,HttpStatus.Series.SERVER_ERROR, "Not Implemented"),
    BAD_GATEWAY(502,HttpStatus.Series.SERVER_ERROR, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503,HttpStatus.Series.SERVER_ERROR, "Service Unavailable"),
    GATEWAY_TIMEOUT(504,HttpStatus.Series.SERVER_ERROR, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505,HttpStatus.Series.SERVER_ERROR, "HTTP Version not supported"),
    VARIANT_ALSO_NEGOTIATES(506,HttpStatus.Series.SERVER_ERROR, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE(507,HttpStatus.Series.SERVER_ERROR, "Insufficient Storage"),
    LOOP_DETECTED(508,HttpStatus.Series.SERVER_ERROR, "Loop Detected"),
    BANDWIDTH_LIMIT_EXCEEDED(509,HttpStatus.Series.SERVER_ERROR, "Bandwidth Limit Exceeded"),
    NOT_EXTENDED(510,HttpStatus.Series.SERVER_ERROR, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED(511,HttpStatus.Series.SERVER_ERROR, "Network Authentication Required");

    private boolean success;
    private int code;
    private String message;

    private ResponseStatus(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    private ResponseStatus(int code, HttpStatus.Series series, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }
}
