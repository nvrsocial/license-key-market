package com.nvrsocial.market.dto.response;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {

    private Instant timestamp;

    private int status;

    private String message;

    private String path;

    private Map<String,String> fields;

    public ApiError() {}

    public ApiError(Instant timestamp, int status, String message, String path, Map<String,String> fields) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.path = path;
        this.fields = fields;
    }
}
