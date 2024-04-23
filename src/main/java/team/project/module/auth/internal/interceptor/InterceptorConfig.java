package team.project.module.auth.internal.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.project.module.auth.export.model.enums.AuthRole;
import team.project.module.user.export.model.datatransfer.UserInfoDTO;
import team.project.module.user.export.model.enums.UserRole;
import team.project.module.user.export.service.UserInfoIService;

import java.util.ArrayList;
import java.util.List;

@Configuration("auth-[SaToken]-InterceptorConfig")
public class InterceptorConfig implements WebMvcConfigurer, StpInterface {

    /* 配置拦截器 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor())/* <- 使用 Sa-Token 提供的综合拦截器 */
            .addPathPatterns("/**");
    }

    /* --------- */

    @Autowired
    UserInfoIService userService;

    /* 返回指定账号 id 所拥有的权限码集合 */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>(); /* 目前系统设计只划分了角色，而没有划分权限，所以在这里返回空列表 */
    }

    /* 返回指定账号 id 所拥有的角色码集合 */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        /* TODO: 缓存 */

        List<String> list = new ArrayList<>();

        String userId = (String)loginId;
        UserInfoDTO userInfo = userService.selectUserRole(userId);
        if (userInfo == null) return list;

        if (userInfo.hasRole(UserRole.STUDENT))      list.add(AuthRole.STUDENT);
        if (userInfo.hasRole(UserRole.TEACHER))      list.add(AuthRole.TEACHER);
        if (userInfo.hasRole(UserRole.CLUB_MEMBER))  list.add(AuthRole.CLUB_MEMBER);
        if (userInfo.hasRole(UserRole.CLUB_MANAGER)) list.add(AuthRole.CLUB_MANAGER);
        if (userInfo.hasRole(UserRole.SUPER_ADMIN))  list.add(AuthRole.SUPER_ADMIN);

        return list;
    }

    public List<String> getRoleListXXX(Object loginId, String loginType) {

        String userId = (String)loginId;

        SaSession session = StpUtil.getSessionByLoginId(userId);
        return session.get("auth", () -> {
            List<String> list = new ArrayList<>();

            UserInfoDTO userInfo = userService.selectUserRole(userId);
            if (userInfo == null) return list;

            if (userInfo.hasRole(UserRole.STUDENT))      list.add(AuthRole.STUDENT);
            if (userInfo.hasRole(UserRole.TEACHER))      list.add(AuthRole.TEACHER);
            if (userInfo.hasRole(UserRole.CLUB_MEMBER))  list.add(AuthRole.CLUB_MEMBER);
            if (userInfo.hasRole(UserRole.CLUB_MANAGER)) list.add(AuthRole.CLUB_MANAGER);
            if (userInfo.hasRole(UserRole.SUPER_ADMIN))  list.add(AuthRole.SUPER_ADMIN);

            return list;
        });
    }
}

