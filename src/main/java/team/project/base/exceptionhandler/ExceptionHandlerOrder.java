package team.project.base.exceptionhandler;

/* 指定异常处理器指定优先级，数值越小优先级越高 */
public class ExceptionHandlerOrder {
    static final int springframeworkExceptionHandler = 1;
    static final int saTokenExceptionHandler         = 2;
    static final int serviceExceptionHandler         = 3;
    static final int mybatisExceptionHandler         = 4;
    static final int generalExceptionHandler         = 5; /* <- 保持最低优先级 */
}
