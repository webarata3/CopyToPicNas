package link.webarata3.dro.copypictonas.util;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class FileUtilTest {
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
