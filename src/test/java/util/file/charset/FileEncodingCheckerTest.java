package util.file.charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FileEncodingCheckerTest {

    FileEncodingChecker fileEncodingChecker;

    @Before
    public void setUp() throws Exception {
        System.out.println("@Before setUp() executed.");
        fileEncodingChecker = new FileEncodingChecker();
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("@After tearDown() executed.");
    }

    @Test(expected = FileNotFoundException.class)
    public void getFileEncodingByNonExistedFile() throws IOException {
        // 존재하지 않는 파일
        File file = new File("");
        fileEncodingChecker.getFileEncoding(file);
        fail("FileNotFoundException must be occurred.");
    }

    @Test
    public void getFileEncoding() throws IOException, URISyntaxException {
        File file;

        // 존재하는 파일, UTF-8(BOM)
        file = new File(this.getClass().getResource("FileEncodingChecker_UTF-8(BOM).txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "UTF-8");

        // 존재하는 파일, UTF-8(BOM)
        file = new File(this.getClass().getResource("FileEncodingChecker_UTF-8(BOM).txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "UTF-8");

        // 존재하는 파일, UTF-8(NO_BOM)
        file = new File(this.getClass().getResource("FileEncodingChecker_UTF-8(NO_BOM).txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "NULL");

        // 존재하는 파일, UNICODE(BE)
        file = new File(this.getClass().getResource("FileEncodingChecker_UNICODE(BE).txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "UTF-16BE");

        // 존재하는 파일, UNICODE(LE)
        file = new File(this.getClass().getResource("FileEncodingChecker_UNICODE(LE).txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "UTF-16LE");

        // 존재하는 파일, ANSI
        file = new File(this.getClass().getResource("FileEncodingChecker_ANSI.txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "NULL");

        // 존재하는 파일, MS949
        file = new File(this.getClass().getResource("FileEncodingChecker_MS949.txt").toURI());
        assertEquals(fileEncodingChecker.getFileEncoding(file), "NULL");
    }

}