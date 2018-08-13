package util.file.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public class CSVFileReaderTest {

    CSVFileReader frs;
    File file;

    @Before
    public void setUp() throws Exception {
        frs = new CSVFileReader();
        file = new File(this.getClass().getResource("CSVFileReaderTestSample.csv").toURI());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void initFile2() {
        frs.initFile(file, StandardCharsets.UTF_8, '\t');
        System.out.println(frs.getMap());
        assertArrayEquals(new String[]{"A03", "삼성전자", "갤럭시노트9"}, frs.getMap().get("A03"));
    }

    @Test
    public void addInterfaceInfoToMap2() {
        frs.addInterfaceInfoToMap("A03\t삼성전자\t갤럭시노트9");
        assertArrayEquals(new String[]{"A03", "삼성전자", "갤럭시노트9"}, frs.getMap().get("A03"));
        frs.getMap().clear();
    }
}