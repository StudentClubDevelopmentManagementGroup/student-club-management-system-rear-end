package team.project.base.exceptionhandler;

/* 指定异常处理器指定优先级，数值越小优先级越高 */
public class ExceptionHandlerOrder {
    static final int SpringframeworkExceptionHandler = 1;
    static final int MybatisExceptionHandler         = 2;
    static final int ServiceExceptionHandler         = 3;
    static final int GeneralExceptionHandler         = 4; /* <- 保持最低优先级 */
}
