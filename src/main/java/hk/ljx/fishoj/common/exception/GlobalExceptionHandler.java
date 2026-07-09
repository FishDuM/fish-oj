package hk.ljx.fishoj.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import hk.ljx.fishoj.common.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .findFirst().map(FieldError::getDefaultMessage).orElse(ErrorCode.VALIDATION_ERROR.getMessage());
        return Result.error(ErrorCode.VALIDATION_ERROR.getCode(), msg);
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleNotLogin(NotLoginException e) {
        return Result.error(ErrorCode.NOT_LOGIN.getCode(), ErrorCode.NOT_LOGIN.getMessage());
    }

    @ExceptionHandler(NotRoleException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleNotRole(NotRoleException e) {
        return Result.error(ErrorCode.NO_PERMISSION.getCode(), ErrorCode.NO_PERMISSION.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("Unhandled exception", e);
        return Result.error(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage() + ": " + e.getMessage());
    }
}