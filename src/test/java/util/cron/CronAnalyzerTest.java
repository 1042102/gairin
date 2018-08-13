package util.cron;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class CronAnalyzerTest {

    @Test
    public void initCronDictionary() throws URISyntaxException {
        CronAnalyzer ca = new CronAnalyzer();
        File file = new File(this.getClass().getResource("CronAnalyzerTestSample.txt").toURI());

        Map<String, String[]> map = ca.initCronDictionary(file);

        assertEquals(5, map.size());
        assertArrayEquals(new String[]{"11,41", "*", "*", "*", "*", "/util/oss/daemon/A01.sh >> xxx.log"}, map.get("oss#A01"));
        assertArrayEquals(new String[]{"11,41", "*", "*", "*", "*", "/util/oss/daemon/B01.sh >> xxx.log"}, map.get("oss#B01"));
    }

    @Test
    public void getId() {
        CronAnalyzer ca = new CronAnalyzer();
        String cmd;
        cmd = "11,41 * * * * /util/oss/daemon/A01.sh >> xxx.log";
        assertEquals("A01", ca.getId(cmd));

        cmd = "31 4 * * * /util/oss/daemon/B02.sh >> xxx.log";
        assertEquals("B02", ca.getId(cmd));
    }

    @Test
    public void getSchedule() {
        CronAnalyzer ca = new CronAnalyzer();

        String cmd;
        cmd = "11,41 * * * * /util/oss/daemon/A01.sh >> xxx.log";
        assertArrayEquals(new String[] {"11,41", "*", "*", "*", "*"}, ca.getSchedule(cmd));

        cmd = "31 4 * * * /util/oss/daemon/B02.sh >> xxx.log";
        assertArrayEquals(new String[] {"31", "4", "*", "*", "*"}, ca.getSchedule(cmd));
    }

    @Test
    public void getCmd() {
        CronAnalyzer ca = new CronAnalyzer();

        String cmd;
        cmd = "11,41 * * * * /util/oss/daemon/A01.sh >> xxx.log";
        assertEquals("/util/oss/daemon/A01.sh >> xxx.log", ca.getCmd(cmd));

        cmd = "31 4 * * * /util/oss/daemon/B02.sh >> xxx.log";
        assertEquals("/util/oss/daemon/B02.sh >> xxx.log", ca.getCmd(cmd));
    }

    @Test
    public void getCompositeId() {
        CronAnalyzer ca = new CronAnalyzer();
        String cmd;
        cmd = "11,41 * * * * /util/oss/daemon/A01.sh >> xxx.log";
        assertEquals("oss#A01", ca.getCompositeId(cmd));

        cmd = "31 4 * * * /util/oss/daemon/B02.sh >> xxx.log";
        assertEquals("oss#B02", ca.getCompositeId(cmd));
    }
}