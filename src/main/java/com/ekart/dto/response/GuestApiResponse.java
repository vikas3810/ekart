package com.ekart.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data

public class GuestApiResponse {
    private boolean status;
    private String message;
    private Object object;

    public GuestApiResponse(boolean status, String message, Object object) {
        this.status = status;
        this.message = message;
        this.object = object;
    }
}
