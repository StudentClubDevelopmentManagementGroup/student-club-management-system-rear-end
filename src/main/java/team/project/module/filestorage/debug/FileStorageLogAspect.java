package team.project.module.filestorage.debug;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class FileStorageLogAspect {

    Logger log = LoggerFactory.getLogger("【fileStorage】");

    @Around("execution(* team.project.module..filestorage.export.service.impl..uploadTextToFile(..))")
    public Object uploadTextToFileLog(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        String text = ((String) args[1]).substring(0, 30);

        try {
            Object result = jp.proceed();
            log.info("""
                 uploadTextToFile
                 text[0, 30] = {}
                 return file_id = {}"""
            , ((String) args[1]).substring(0, 30), result);
            return result;

        } catch (Exception e) {
            log.info("""
                 uploadTextToFile
                 text[0, 30] = {}
                 throw exception = {}"""
            , text, e.getMessage(), e);
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
            , fileId, result.substring(0, 30));
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
