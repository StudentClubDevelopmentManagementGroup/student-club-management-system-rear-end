package team.project.module.user.internal.util;

import java.util.Random;

public class Util {

    /* 校验码的字符池，剔除部分易混淆的数字和字母 */
    private static final String VERIFICATION_CODE_CHAR_POOL = "23456789ABCDEFGHJKLMNPQRSTWXY";

    /**
     * 生成指定长度的校验码
     * */
    public static String randomVerificationCode(int length) {
        assert length > 0;

        Random random = new Random();
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            int chrIdx = random.nextInt(VERIFICATION_CODE_CHAR_POOL.length());
            result[i] = VERIFICATION_CODE_CHAR_POOL.charAt(chrIdx);
        }
        return new String(result);
    }
}
