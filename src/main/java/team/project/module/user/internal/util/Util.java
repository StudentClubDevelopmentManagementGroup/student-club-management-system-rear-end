package team.project.module.user.internal.util;

import java.util.Random;

public class Util {

    /* 校验码的字符池，剔除数字 0、1 和字母 O、L */
    private static final String VERIFICATION_CODE_CHAR_POOL = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * 生成指定长度的校验码
     * */
    public static String randomVerificationCode(int length) {
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(VERIFICATION_CODE_CHAR_POOL.length());
            randomString.append(VERIFICATION_CODE_CHAR_POOL.charAt(idx));
        }
        return randomString.toString();
    }
}
