package team.project.base.controller.exception;

import lombok.Getter;

/**
 * <p>自定义的 controller 层异常类
 * <p>入参校验失败时抛出此异常
 * */
@Getter
public class InvalidParamException extends RuntimeException {
    public InvalidParamException(String message) {
        super(message);
    }
}
