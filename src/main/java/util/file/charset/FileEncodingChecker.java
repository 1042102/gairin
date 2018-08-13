package util.file.charset;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileEncodingChecker {

    /**
     * BOM이 있다면, 인코딩: 표현(16진수)
     * UTF-8: EF BB BF
     * UTF-16(BE): FE FF
     * UTF-16(LE): FF FE
     * UTF-32(BE): 00 00 FE FF
     * UTF-32(LE): FF FE 00 00
     * UTF-7:
     *     2B 2F 76 38
     *     2B 2F 76 39
     *     2B 2F 76 2F
     *     2B 2F 76 38 2D
     * UTF-1: F7 64 4C
     * UTF-EBCDIC: DD 73 66 73
     * SCSU: 0E FE FF
     * BOCU-1: FE EE 28
     * GB-18030: 84 31 95 33
     *
     * BOM이 없다면, 인코딩을 특정할 수 없다.
     * UTF-8인 경우에도 BOM이 없는 경우가 있다.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String getFileEncoding(File file) throws IOException {
        try {
            byte[] bytes = new byte[4];
            InputStream is = new FileInputStream(file);
            is.read(bytes);

            byte[] in2Bytes = Arrays.copyOfRange(bytes, 0, 2);
            byte[] in3Bytes = Arrays.copyOfRange(bytes, 0, 3);
            byte[] in4Bytes = Arrays.copyOfRange(bytes, 0, 4);

            System.out.println("File: " + file.getName());
            System.out.print("\t 2Bytes: ");
            for (byte b: in2Bytes) {
                System.out.printf("[%x %d] ", b, b);
            }
            System.out.println();
            System.out.print("\t 3Bytes: ");
            for (byte b: in3Bytes) {
                System.out.printf("[%x %d] ", b, b);
            }
            System.out.println();
            System.out.print("\t 4Bytes: ");
            for (byte b: in4Bytes) {
                System.out.printf("[%x %d] ", b, b);
            }
            System.out.println();

            // 2Byte
            byte[] bUTF_16BE = new byte[]{(byte)0xFE, (byte)0xFF};
            byte[] bUTF_16LE = new byte[]{(byte)0xFF, (byte)0xFE};

            if (Arrays.equals(in2Bytes, bUTF_16BE)) return StandardCharsets.UTF_16BE.displayName();
            if (Arrays.equals(in2Bytes, bUTF_16LE)) return StandardCharsets.UTF_16LE.displayName();

            // 3Byte
            byte[] bUTF_8 = new byte[]{(byte)0xEF, (byte)0xBB, (byte)0xBF};
            byte[] bUTF_7 = new byte[]{(byte)0x2B, (byte)0x2F, (byte)0x76};
            byte[] bUTF_1 = new byte[]{(byte)0xF7, (byte)0x64, (byte)0x4C};
            byte[] bSCSU = new byte[]{(byte)0x0E, (byte)0xFE, (byte)0xFF};
            byte[] bBOCU_1 = new byte[]{(byte)0xFE, (byte)0xEE, (byte)0x28};

            if (Arrays.equals(in3Bytes, bUTF_8)) return StandardCharsets.UTF_8.displayName();
            if (Arrays.equals(in3Bytes, bUTF_7)) return "UTF-7";
            if (Arrays.equals(in3Bytes, bUTF_1)) return "UTF_1";
            if (Arrays.equals(in3Bytes, bSCSU)) return "SCSU";
            if (Arrays.equals(in3Bytes, bBOCU_1)) return "BOCU_1";

            // 4Byte
            byte[] bUTF_32BE = new byte[]{(byte)0x00, (byte)0x00, (byte)0xFE, (byte)0xFF};
            byte[] bUTF_32LE = new byte[]{(byte)0xFF, (byte)0xFE, (byte)0x00, (byte)0x00};
            byte[] bUTF_EBCDIC = new byte[]{(byte)0xDD, (byte)0x73, (byte)0x66, (byte)0x73};
            byte[] bGB_18030 = new byte[]{(byte)0x84, (byte)0x31, (byte)0x95, (byte)0x33};

            if (Arrays.equals(in4Bytes, bUTF_32BE)) return "UTF-32BE";
            if (Arrays.equals(in4Bytes, bUTF_32LE)) return "UTF-32LE";
            if (Arrays.equals(in4Bytes, bUTF_EBCDIC)) return "UTF-EBCDIC";
            if (Arrays.equals(in4Bytes, bGB_18030)) return "GB_18030";

            return "NULL";

        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
    }
}
