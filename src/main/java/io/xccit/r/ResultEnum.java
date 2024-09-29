package io.xccit.r;

import lombok.Getter;

/**
 * @author CH_ywx
 * @version 1.0
 * @create 2024/9/29
 * <p>返回类型枚举</p>
 */
@Getter
public enum ResultEnum {
    SUCCESS(200,"SUCCESS"),
    SERVER_ERROR(500,"SERVER ERROR"),
    NOT_FOUND(404,"NOT FOUND"),
    BAD_REQUEST(400,"BAD REQUEST"),
    UNAUTHORIZED(401,"UNAUTHORIZED"),
    FORBIDDEN(403,"FORBIDDEN"),
    NOT_SUPPORT(405,"NOT SUPPORT"),
    NOT_ACCEPTABLE(406,"NOT ACCEPTABLE"),
    UNSUPPORTED_MEDIA_TYPE(415,"UNSUPPORTED MEDIA TYPE");
    private final Integer code;
    private final String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
