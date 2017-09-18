package link.webarata3.dro.copypictonas.util;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class FileUtilTest {
    @RunWith(Theories.class)
    public static class 正常系_getDisplayFileSize {
        @DataPoints
        public static Fixture[] PARAMs = {
            new Fixture(999, "999B"),
            new Fixture(1000, "0.977KB"),
            new Fixture(1001, "0.978KB"),
            new Fixture(1024, "1.00KB"),
            new Fixture(1029, "1.01KB"),
            new Fixture(1024 * 1024, "1.00MB"),
            new Fixture(1024 * 1024 * 1024, "1.00GB"),
            new Fixture(1024L * 1024L * 1024L * 1024L, "1.00TB")
        };

        static class Fixture {
            long size;
            String displaySize;

            Fixture(long size, String displaySize) {
                this.size = size;
                this.displaySize = displaySize;
            }

            @Override
            public String toString() {
                return "Fixture{" +
                    "size=" + size +
                    ", displaySize='" + displaySize + '\'' +
                    '}';
            }
        }

        @Theory
        public void test(Fixture fixture) {
            assertThat(FileUtil.getDisplayFileSize(fixture.size), is(fixture.displaySize));
        }
    }

    @RunWith(Theories.class)
    public static class 正常系_fileCountAndSize {
        @DataPoints
        public static Fixture[] PARAMs = {
            new Fixture("dummy1", true, true, new File[] {
                getMockFile("dummy.txt", false, 100)
            }, 1, 100L),
            new Fixture("dummy1", true, true, new File[] {
                getMockFile("dummy.txt", false, 100),
                getMockFile("dummy.txt", false, 200)
            }, 2, 300L),
            new Fixture("dummy1", true, true, new File[] {
                getMockFile("dummy.txt", false, 100),
                getMockFile("dummy.txt", true, 200),
                getMockFile("dummy.txt", false, 200)
            }, 2, 300L)

        };

        static class Fixture {
            File dir;
            int count;
            long size;

            Fixture(String dirName, boolean exists, boolean isDirectory, File[] files,
                    int count, long size) {
                File dir = Mockito.mock(File.class, dirName);
                Mockito.when(dir.exists()).thenReturn(exists);
                Mockito.when(dir.isDirectory()).thenReturn(isDirectory);
                Mockito.when(dir.listFiles()).thenReturn(files);

                this.dir = dir;
                this.count = count;
                this.size = size;
            }

            @Override
            public String toString() {
                return "Fixture{" +
                    "dir=" + dir +
                    ", count=" + count +
                    ", size=" + size +
                    '}';
            }
        }


        private static File getMockFile(String fileName, boolean isDirectory, long size) {
            File file = Mockito.mock(File.class, fileName);
            Mockito.when(file.isDirectory()).thenReturn(isDirectory);
            Mockito.when(file.length()).thenReturn(size);

            return file;
        }

        @Theory
        public void test(Fixture fixture) {
            assertThat(FileUtil.fileCountAndSize(fixture.dir).getCount(), is(fixture.count));
            assertThat(FileUtil.fileCountAndSize(fixture.dir).getSize(), is(fixture.size));
        }
    }

    public static class 異常系_fileCountAndSize {
        @Test
        public void test() {
            File dir = Mockito.mock(File.class, "ファイルなし");
            Mockito.when(dir.exists()).thenReturn(false);

        }
    }
}
