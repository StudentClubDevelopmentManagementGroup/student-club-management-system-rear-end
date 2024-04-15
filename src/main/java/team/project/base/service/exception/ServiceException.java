package team.project.base.service.exception;

import lombok.Getter;
import lombok.ToString;
import team.project.base.service.status.ServiceStatus;

/* 自定义的 service 层异常类 */
@ToString
@Getter
public class ServiceException extends RuntimeException {
    private final ServiceStatus status;

    public ServiceException(ServiceStatus status, String message) {
        super(message);
        this.status = status;
    }
}
