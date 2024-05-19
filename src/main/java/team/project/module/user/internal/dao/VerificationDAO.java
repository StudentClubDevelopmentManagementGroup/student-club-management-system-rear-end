package team.project.module.user.internal.dao;

import org.springframework.stereotype.Component;
import team.project.util.expiringmap.ExpiringMap;

import java.util.concurrent.TimeUnit;

@Component
public class VerificationDAO {

    /* 验证码的有效时长 5 分钟 */
    private static final long EXPIRATION_TIME_MILLIS = TimeUnit.MINUTES.toMillis(5);

    /* 验证码的有效时长小于 4 分钟后，可以再次发送邮件（避免两次发送邮件的时间间隔过短） */
    private static final long CAN_SEND_CODE_AGAIN_TIME_MILLIS = TimeUnit.MINUTES.toMillis(4);

    /* 存储验证码（暂时将验证码存储在内存而不是数据库） */
    private final ExpiringMap<String, String> map = new ExpiringMap<>(EXPIRATION_TIME_MILLIS);

    /**
     * 添加一对用户名和验证码
     * */
    public void put(String userId, String code) {
        map.put(userId, code);
    }

    /**
     * 校验用户名和验证码，如果校验通过则删除验证码
     * */
    public boolean verify(String userId, String code) {
        String storedCode = map.get(userId);

        if (storedCode == null || ! storedCode.equals(code))
            return false;

        map.remove(userId);
        return true;
    }

    /**
     * 判断能否再次发送邮件
     * */
    public boolean canSendCodeAgain(String userId) {
        return map.getRemainingTime(userId) <= CAN_SEND_CODE_AGAIN_TIME_MILLIS;
    }
}
