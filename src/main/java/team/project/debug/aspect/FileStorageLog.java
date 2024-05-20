package team.project.debug.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FileStorageLog {

    private static final Logger log = LoggerFactory.getLogger("【file storage log aspect 文件存储日志切面】");

    private String subStr30(String str) {
        if (str.length() > 30) {
            return str.substring(0, 30);
        }
        return str;
    }

    @Around("execution(* team.project.module..filestorage.export.service.impl..uploadTextToFile(..))")
    public Object uploadTextToFileLog(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        try {
            Object result = jp.proceed();
            log.info("""
                 uploadTextToFile
                 text[0, 30] = {}
                 return file_id = {}"""
            , subStr30((String) args[1]), result);
            return result;

        } catch (Exception e) {
            log.info("""
                 uploadTextToFile
                 text[0, 30] = {}
                 throw exception = {}"""
            , subStr30((String) args[1]), e.getMessage(), e);
            throw e;
        }
    }

    @Around("execution(* team.project.module..filestorage.export.service.impl..getTextFromFile(..))")
    public Object getTextFromFileLog(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        String fileId = ((String) args[0]);

        String result = (String)(jp.proceed());
        if (result != null) {
            log.info("""
                 getTextFromFile
                 file_id = {}
                 return text[0, 30] = {}"""
            , fileId, subStr30(result));
        }
        else {
            log.info("""
                 getTextFromFile
                 file_id = {}
                 return null"""
            , fileId);
        }
        return result;
    }

    @Around("execution(* team.project.module..filestorage.export.service.impl..deleteFile(..))")
    public Object deleteFileLog(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        String fileId = ((String) args[0]);

        boolean result = (boolean)(jp.proceed());
        if (result) {
            log.info("""
                deleteFileLog
                file_id = {}
                return true (file deleted)"""
            , fileId);
        }
        else {
            log.info("""
                deleteFileLog
                file_id = {}
                return false (file deleted failed)"""
            , fileId);
        }
        return result;
    }
}
