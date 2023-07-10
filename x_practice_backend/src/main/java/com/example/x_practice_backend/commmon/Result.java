package com.example.x_practice_backend.commmon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 公共响应类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;   // 状态码
    private String message; // 描述信息
    private T data; // 返回的数据

    // 最简单的成功返回
    public static <T> Result<T> success() {
        return new Result<>(20000, "success", null);
    }

    // 带数据的成功返回
    public static <T> Result<T> success(T data) {
        return new Result<>(20000, "success", data);
    }

    // 带消息、数据的成功返回
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(20000, message, data);
    }

    // 只带消息的成功返回
    public static <T> Result<T> success(String message) {
        return new Result<>(20000, message, null);
    }

    // 最简单的失败返回
    public static <T> Result<T> fail() {
        return new Result<>(20001, "fail", null);
    }

    // 指定返回码的失败返回
    public static <T> Result<T> fail(Integer code) {
        return new Result<>(code, "fail", null);
    }

    // 指定返回码、带消息的失败返回
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // 指定消息的失败返回
    public static <T> Result<T> fail(String message) {
        return new Result<>(20001, message, null);
    }
}
