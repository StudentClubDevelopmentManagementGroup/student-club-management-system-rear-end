package team.project.module.user.internal.config;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuth implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<String>();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> list = new ArrayList<>();
        if ("2100301514".equals((String)loginId)) {
            list.add("ljh");
        }
        return list;
    }

}

