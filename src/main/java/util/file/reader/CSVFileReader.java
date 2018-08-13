package util.file.reader;

import com.opencsv.CSVReader;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVFileReader {

    private Map<String, String[]> map = new HashMap();

    public Map<String, String[]> getMap() {
        return map;
    }

    public void initFile(File file) {
        CSVReader reader;
        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(file)));
            List<String[]> list = reader.readAll();
            for (String[] lines: list) {
                addInterfaceInfoToMap(lines);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initFile(File file, Charset charset, Character separator) {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(file), charset), separator);
            List<String[]> list = reader.readAll();
            for (String[] lines: list) {
                addInterfaceInfoToMap(lines);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInterfaceInfoToMap(String cmd) {
        String[] interfaceInfo = cmd.split("\t");
        if (map.containsKey(interfaceInfo[0])) {
            map.put(interfaceInfo[0]+"#", interfaceInfo);
        } else {
            map.put(interfaceInfo[0], interfaceInfo);
        }
    }

    public void addInterfaceInfoToMap(String[] cmd) {
        if (map.containsKey(cmd[0])) {
            map.put(cmd[0]+"#", cmd);
        } else {
            map.put(cmd[0], cmd);
        }
    }
}
