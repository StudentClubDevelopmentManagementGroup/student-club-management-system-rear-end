package team.project.module.auth.internal.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoServiceI;

import java.util.ArrayList;
import java.util.List;

@Configuration("auth-[SaToken]-InterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer, StpInterface {

    /* -- 配置拦截器 -- */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())/* <- 使用 Sa-Token 提供的综合拦截器 */
            .addPathPatterns("/**");
    }

    /* -- 校验权限和身份 -- */

    /**
     * 返回指定账号 id 所拥有的权限码集合
     * */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return null; /* 目前系统设计只划分了角色，而没有划分权限，所以在这里返回空列表 */
    }

    @Autowired
    UserInfoServiceI userService;

    private final List<String> allRoles = List.of(
        AuthRole.CLUB_MEMBER,
        AuthRole.CLUB_MANAGER,
        AuthRole.SUPER_ADMIN
    );

    /**
     * 返回指定账号 id 所拥有的角色码集合
     * */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        assert loginId != null;

        List<String> list = new ArrayList<>();

        String userId = (String)loginId;
        Integer userRole = userService.selectUserRole(userId); /* <- user 模块对此查询已有缓存优化 */
        if (userRole == null) return list;

        if (UserRole.hasRole(userRole, UserRole.SUPER_ADMIN))  return allRoles;
        if (UserRole.hasRole(userRole, UserRole.CLUB_MEMBER))  list.add(AuthRole.CLUB_MEMBER);
        if (UserRole.hasRole(userRole, UserRole.CLUB_MANAGER)) list.add(AuthRole.CLUB_MANAGER);

        return list;
    }
}
