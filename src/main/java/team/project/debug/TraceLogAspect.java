package team.project.debug;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect /* <-- 开启切面 debug */
public class TraceLogAspect {
    Logger logger = LoggerFactory.getLogger(TraceLogAspect.class);

    /* 输出项目中所有包下的所有方法调用信息 */
    @Around("execution(* team.project.module..controller..*(..)) ||" +
            "execution(* team.project.module..service..*(..))    ||" +
            "execution(* team.project.module..mapper..*(..))       " )
    public Object logMethodExecution(ProceedingJoinPoint jp) throws Throwable {
        Signature signature       = jp.getSignature();
        String    simpleClassName = signature.getDeclaringType().getSimpleName();
        String    className       = signature.getDeclaringTypeName();
        String    methodName      = signature.getName();
        Object[]  args            = jp.getArgs();

        try {
            Object result = jp.proceed();
            logger.info(
                "   class  : {} ( {} )\n" +
                "   method : {}\n"        +
                "   args   : {}\n"        +
                "   return : {}"          ,
                simpleClassName, className, methodName, args, result
            );
            return result;

        } catch (Exception e) {
            logger.info(
                "   class  : {} ( {} )\n" +
                "   method : {}\n"        +
                "   args   : {}\n"        +
                " ! throw  : {}"          ,
                simpleClassName, className, methodName, args, e.getClass().getName()
            );
            throw e;
        }
    }
}
