package com.realtimeeventticketingsystem.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class StandardResponse {
    private int status;
    private String message;
    private Object data;

    public StandardResponse(int i, String configureSystemStatus, Map<String, Object> data) {
    }
}
