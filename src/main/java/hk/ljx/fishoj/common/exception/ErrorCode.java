package hk.ljx.fishoj.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(20000, "成功"),

    PARAMS_ERROR(40000, "请求参数错误"),
    PARAMS_MISSING(40001, "请求参数缺失"),
    VALIDATION_ERROR(40002, "参数校验失败"),

    NOT_LOGIN(40100, "未登录"),
    TOKEN_EXPIRED(40101, "登录已过期"),
    TOKEN_INVALID(40102, "无效的令牌"),
    NO_PERMISSION(40300, "无权限访问"),

    NOT_FOUND(40400, "资源不存在"),
    USER_NOT_FOUND(40401, "用户不存在"),
    PROBLEM_NOT_FOUND(40402, "题目不存在"),
    JUDGE_RESULT_NOT_FOUND(40403, "判题结果不存在"),
    TEST_CASE_NOT_FOUND(40404, "测试用例不存在"),
    TAG_NOT_FOUND(40405, "标签不存在"),
    SUBMIT_NOT_FOUND(40406, "提交记录不存在"),
    USER_PROBLEM_NOT_FOUND(40407, "用户题目记录不存在"),

    USER_PASSWORD_ERROR(50001, "用户名或密码错误"),
    USER_ALREADY_EXISTS(50002, "用户名已存在"),

    SYSTEM_ERROR(50000, "服务器内部错误");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}