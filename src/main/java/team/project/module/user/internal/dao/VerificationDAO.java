package team.project.module.user.internal.dao;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static team.project.module.user.internal.dao.VerificationDAO.VerificationCode.EXPIRATION_MINUTES;

@Component
public class VerificationDAO {

    /**
     * 验证码
     * */
    static class VerificationCode {
        public String        code;
        public LocalDateTime expireTime;

        public static final long EXPIRATION_MINUTES = 5; /* <- 有效期 5 分钟 */
    }

    /**
     * 存储用户名和验证码的
     * */
    private final Map<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();

    /**
     * 添加一对用户名和验证码
     * */
    public void put(String userId, String code) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.code = code;
        verificationCode.expireTime = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        verificationCodes.put(userId, verificationCode);
    }

    /**
     * 校验用户名和验证码
     * */
    public boolean verify(String userId, String code) {
        cleanUpExpiredCodes();

        VerificationCode verificationCode = verificationCodes.get(userId);

        if (verificationCode == null) {
            return false;
        }
        else if ( ! verificationCode.code.equals(code)) {
            return false;
        }
        else {
            verificationCodes.remove(userId);
            return true;
        }
    }

    /**
     * 清除过期的验证码
     * */
    private void cleanUpExpiredCodes() {
        LocalDateTime now = LocalDateTime.now();

        verificationCodes.entrySet().removeIf(
            entry -> {
                VerificationCode code = entry.getValue();
                return code.expireTime.isBefore(now);
            }
        );
    }
}

