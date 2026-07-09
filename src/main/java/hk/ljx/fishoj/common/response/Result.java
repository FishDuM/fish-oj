package hk.ljx.fishoj.common.response;

import hk.ljx.fishoj.common.exception.ErrorCode;
import lombok.Data;

@Data
public class Result<T> {

    private int code;
    private String message;
    private T data;

    private Result() {}

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = ErrorCode.SUCCESS.getCode();
        r.message = ErrorCode.SUCCESS.getMessage();
        r.data = data;
        return r;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> Result<T> error(String message) {
        return error(ErrorCode.SYSTEM_ERROR.getCode(), message);
    }
}
