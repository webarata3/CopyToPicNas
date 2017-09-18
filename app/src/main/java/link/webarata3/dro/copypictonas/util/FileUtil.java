package link.webarata3.dro.copypictonas.util;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class FileUtil {
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

    public static class TotalInDirectory {
        private final int count;
        private final long size;

        public TotalInDirectory(int count, long size) {
            this.count = count;
            this.size = size;
        }

        public int getCount() {
            return count;
        }

        public long getSize() {
            return size;
        }
    }

    public static TotalInDirectory fileCountAndSize(File dir) {
        if (!dir.exists()) {
            throw new IllegalArgumentException(dir.getName() + "がありません");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir.getName() + "はディレクトリーではありません");
        }

        File[] files = dir.listFiles();
        int fileCount = 0;
        long fileSize = 0;
        for (File localFile : files) {
            if (!localFile.isDirectory()) {
                fileCount++;
                try {
                    fileSize += localFile.length();
                } catch (Exception e) {
                    // ignore?
                }
            }
        }

        return new TotalInDirectory(fileCount, fileSize);
    }
}
