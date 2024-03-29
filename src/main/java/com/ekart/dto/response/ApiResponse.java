package com.ekart.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ApiResponse {
    private Integer code;
    private String message;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    public ApiResponse()
    {
        this.timestamp=LocalDateTime.now();
    }

    private Object data;

    public ApiResponse(Integer code, String message, Object data) {

        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ApiResponse(Integer code, String message) {

        this();
        this.code = code;
        this.message = message;
    }
}
