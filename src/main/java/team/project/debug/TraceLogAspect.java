package team.project.debug;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
// @Aspect /* <-- 开启切面 debug */
public class TraceLogAspect {

    Logger controllerLogger = LoggerFactory.getLogger("【controller 层】");
    Logger serviceLogger    = LoggerFactory.getLogger("【service 层】");
    Logger daoLogger        = LoggerFactory.getLogger("【dao 层】");
    Logger mapperLogger     = LoggerFactory.getLogger("【mapper 层】");

    @Around("execution(* team.project.module..controller..*(..))")
    public Object controllerLog(ProceedingJoinPoint jp) throws Throwable {
        return traceLog(jp, controllerLogger);
    }

    @Around("execution(* team.project.module..service..*(..))")
    public Object serviceLog(ProceedingJoinPoint jp) throws Throwable {
        return traceLog(jp, serviceLogger);
    }

    @Around("execution(* team.project.module..mapper..*(..))")
    public Object mapperLog(ProceedingJoinPoint jp) throws Throwable {
        return traceLog(jp, mapperLogger);
    }

    @Around("execution(* team.project.module..dao..*(..))")
    public Object daoLog(ProceedingJoinPoint jp) throws Throwable {
        return traceLog(jp, daoLogger);
    }

    private Object traceLog(ProceedingJoinPoint jp, Logger logger) throws Throwable {
        Signature signature       = jp.getSignature();
        String    simpleClassName = signature.getDeclaringType().getSimpleName();
        String    className       = signature.getDeclaringTypeName();
        String    methodName      = signature.getName();
        Object[]  args            = jp.getArgs();

        try {
            Object result = jp.proceed();
            logger.info("""
                   类： {} ( {} )
                   方法： {}
                   入参： {}
                   返回： {}\
                """,
                simpleClassName, className, methodName, args, result
            );
            return result;

        } catch (Exception e) {
            logger.info("""
                   类： {} ( {} )
                   方法： {}
                   入参： {}
                 ! 抛出： {}\
                """,
                simpleClassName, className, methodName, args, e.getClass().getName()
            );
            throw e;
        }
    }
}
