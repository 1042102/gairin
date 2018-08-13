package util.cron;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CronAnalyzer {

    private Map<String, String[]> cronMap;

    public Map<String, String[]> getCronMap() {
        return cronMap;
    }

    /**
     * crontab에 등록된 명령어 중 동일한 BlahBlah.sh이 여러 개인 경우 사용한다.
     * 쉘 명으로는 중복이 발생하므로, 2번째 상위 디렉토리명과 결합한 값을 리턴한다.
     *
     * ex)
     * /util/sample1/dir/cmd1.sh -> sample1#cmd1
     * /util/sample2/dir/cmd1.sh -> sample2#cmd1
     *
     * @param cmd 주기를 포함한 crontab 명령어
     * @return
     */
    public String getCompositeId(String cmd) {
        cmd.trim();
        int startPoint = cmd.substring(0, cmd.indexOf(".sh")).lastIndexOf(" ");
        int finishPoint = cmd.indexOf(".sh");

        String[] dirs = cmd.substring(startPoint+1, finishPoint).split("/");

        return dirs[dirs.length-3] + "#" + dirs[dirs.length-1];
    }


    /**
     * crontab에 등록된 명령어 중 BlahBlah.sh로 끝나는 쉘이 유일한 경우 사용한다.
     * crontab에 등록된 명령어 중 "/"와 ".sh"로 감싸진 단어를 리턴한다.
     *
     * ex)
     * /util/sample/dir/cmd1.sh -> cmd1
     * /util/sample/dir/cmd2.sh -> cmd2
     *
     * @param cmd 주기를 포함한 crontab 명령어
     * @return
     */
    public String getId(String cmd) {
        cmd.trim();
        int startPoint = cmd.substring(0, cmd.indexOf(".sh")).lastIndexOf("/");
        int finishPoint = cmd.indexOf(".sh");
        return cmd.substring(startPoint+1, finishPoint);
    }


    /**
     * crontab에 등록된 명령어 중 스케쥴을 String 배열로 리턴한다.
     *
     * ex)
     * cmd = "0 4 * * * . /util/sample/dir/cmd1.sh"; -> new String[] {"0", "4", "*", "*", "*"}
     *
     * @param cmd 주기를 포함한 crontab 명령어
     * @return
     */
    public String[] getSchedule(String cmd) {
        cmd.trim();
        String[] result = cmd.split(" ", 6);
        String[] schedule = Arrays.copyOfRange(result, 0, 5);

        return schedule;
    }

    /**
     * crontab에 등록된 명령어 중 스케쥴을 제외한 명령어 부분을 리턴한다.
     *
     * ex)
     * cmd = "0 4 * * * . /util/sample/dir/cmd1.sh"; -> ". /util/sample/dir/cmd1.sh"
     *
     * @param cmd 주기를 포함한 crontab 명령어
     * @return
     */
    public String getCmd(String cmd) {
        cmd.trim();
        String[] result = cmd.split(" ", 6);
        String cronCmd = result[5].trim();

        return cronCmd;
    }

    /**
     *
     *
     * @param cmd 주기를 포함한 crontab 명령어
     * @return
     */
    public String[] getCronExpArray(String cmd) {
        cmd.trim();
        String[] result = cmd.split(" ", 6);
        return result;
    }

    /**
     *
     *
     * @param file
     * @return
     */
    public Map<String, String[]> initCronDictionary(File file) {
        cronMap = new HashMap();

        try (Stream<String> lines = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8); Stream<String> cronJob = lines.filter(k -> !(k.startsWith("#"))).filter(k -> k.length() > 10)) {
            cronJob.forEach(x -> {
                addCronMap(x);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cronMap;
    }

    /**
     *
     *
     * @param cmd 주기를 포함한 crontab 명령어
     */
    public void addCronMap(String cmd) {
        // TODO: 동일한 연계ID가 있을 수도 있는데, 이렇게 처리하는 것이 최선일까?
        // - 중복키를 별도로 관리해서 후처리하도록 할까?
        if (cronMap.containsKey(getId(cmd))) {
            cronMap.put(getCompositeId(cmd)+"#", getCronExpArray(cmd));
        } else {
            cronMap.put(getCompositeId(cmd), getCronExpArray(cmd));
        }
    }
}
