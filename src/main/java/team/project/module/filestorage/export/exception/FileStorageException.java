package team.project.module.filestorage.export.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

@Getter
public class FileStorageException extends ServiceException {

    @AllArgsConstructor
    public enum Status {
        FILE_EXIST,     /* 文件已存在 */
        UNSOLVABLE,     /* 无法解决的异常 */
    }

    private final Status fileStorageExceptionStatus;

    public FileStorageException(Status status) {
        super(ServiceStatus.INTERNAL_SERVER_ERROR, null);
        this.fileStorageExceptionStatus = status;
    }
}
