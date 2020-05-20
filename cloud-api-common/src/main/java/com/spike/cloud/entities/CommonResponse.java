package com.spike.cloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author spike
 * @Date: 2020-05-12 22:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    public final static String SUCCESS = "success";
    public final static String FAIL = "fail";
    String status;
    String msg;
    T data;
    public CommonResponse Success(T data) {
        return new CommonResponse(SUCCESS, null, data);
    }
    public CommonResponse Fail(String msg, T data) {
        return new CommonResponse(FAIL, msg, data);
    }
}
