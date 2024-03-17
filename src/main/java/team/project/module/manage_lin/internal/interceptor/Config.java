package team.project.module.manage_lin.internal.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.project.module._template.internal.interceptor.TmplInterceptor;

@Configuration
public class Config implements WebMvcConfigurer {

    @Autowired
    TmplInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        /* 配置拦截器 */
        InterceptorRegistration registration = registry.addInterceptor(interceptor);

        /* 设置拦截的路径
        registration.addPathPatterns( ... );
        */

        /* 设置不拦截的路径
        registration.excludePathPatterns( ... );
        */
    }
}
