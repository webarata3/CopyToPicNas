package link.webarata3.dro.copypictonas.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class FileSizeUtil {
    private static final String[] UNIT_NAMES = {"B", "KB", "MB", "GB", "TB", "PB"};

    private static final BigDecimal SEPARATE = BigDecimal.valueOf(1000);
    private static final BigDecimal DIVIDE = BigDecimal.valueOf(1024);

    /**
     * ファイルのサイズを表示用に加工して返す。
     *
     * @param size サイズ
     * @return 加工したサイズ
     */
    public static String getDisplayFileSize(long size) {
        BigDecimal resultSize = BigDecimal.valueOf(size).setScale(3, RoundingMode.HALF_UP);
        int i;
        for (i = 0; i < UNIT_NAMES.length; i++) {
            if (resultSize.compareTo(SEPARATE) < 0) {
                break;
            }
            resultSize = resultSize.divide(DIVIDE, BigDecimal.ROUND_HALF_UP);
        }

        resultSize = resultSize.round(new MathContext(3, RoundingMode.HALF_UP));

        return resultSize.toPlainString() + UNIT_NAMES[i];
    }
}
