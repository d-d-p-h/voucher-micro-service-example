package com.example.vhandler.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseBody<T> {

    private int statusCode;
    private String message;
    private T result;
}
