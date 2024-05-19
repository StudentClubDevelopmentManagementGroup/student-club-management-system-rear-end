package team.project.module.user.internal.util;

import java.util.Random;

public class VerificationCodeUtil {

    private static final String CHAR_POOL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 生成指定长度的校验码
     * */
    public static String randomCode(int length) {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            randomString.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return randomString.toString();
    }
}
