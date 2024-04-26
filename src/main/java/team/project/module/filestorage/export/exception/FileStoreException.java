package team.project.module.filestorage.export.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import team.project.base.service.exception.ServiceException;
import team.project.base.service.status.ServiceStatus;

@Getter
public class FileStoreException extends ServiceException {

    @AllArgsConstructor
    public enum Status {
        FILE_EXIST,     /* 文件已存在 */
        UNSOLVABLE,     /* 无法解决 */
        ;
    }

    Status status;

    public FileStoreException(Status status) {
        super(ServiceStatus.INTERNAL_SERVER_ERROR, null);
        this.status = status;
    }
}
