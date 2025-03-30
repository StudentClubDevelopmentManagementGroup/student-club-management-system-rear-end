package team.project.module.util.time;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
public class dateTimeUtils {
    /**
     * 计算两个 LocalDateTime 之间的自然日差异
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差（正数表示 end 在 start 之后）
     */
    public static long calculateDaysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(
                start.toLocalDate(),
                end.toLocalDate()
        );
    }
    /**
     * 计算精确的天数差（基于24小时）
     * @param start 开始时间
     * @param end 结束时间
     * @return 天数差（正数表示 end 在 start 之后）
     */
    public static long calculateExactDaysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
