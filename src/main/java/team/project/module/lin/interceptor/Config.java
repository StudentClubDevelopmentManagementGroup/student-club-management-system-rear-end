package team.project.module.lin.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Autowired
    TmplInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /* 配置“只有本模块会使用”到的拦截器 */
        InterceptorRegistration registration = registry.addInterceptor(interceptor);

        /* 设置拦截的路径
        registration.addPathPatterns( ... );
        */

        /* 设置不拦截的路径
        registration.excludePathPatterns( ... );
        */
    }
}
