package link.webarata3.dro.copypictonas;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class FileSizeUtil {
    private static final String[] UNIT_NAMES = {"B", "KB", "MB", "GB", "TB",
            "PB"};

    private static final BigDecimal SEPARATE = new BigDecimal(1000);
    private static BigDecimal DIVIDE = new BigDecimal(1024);

    /**
     * ファイルのサイズを表示用に加工して返す。
     *
     * @param size サイズ
     * @return 加工したサイズ
     */
    public static String getFileSizeForView(long size) {
        BigDecimal resultSize = new BigDecimal(size);
        resultSize.setScale(4);
        int i;
        for (i = 0; i < UNIT_NAMES.length; i++) {
            if (resultSize.compareTo(SEPARATE) < 0) {
                break;
            }
            resultSize = resultSize.divide(DIVIDE);
        }

        resultSize = resultSize.round(new MathContext(3, RoundingMode.HALF_UP));

        return resultSize.toPlainString() + UNIT_NAMES[i];
    }
}
