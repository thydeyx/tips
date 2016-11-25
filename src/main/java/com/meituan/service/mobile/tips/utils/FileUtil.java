package com.meituan.service.mobile.tips.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Is file exist?
     *
     * @param path file path
     * @return true ? or false ?
     */
    public static boolean exist(String path) {
        File f = new File(path);
        return f.exists();
    }

    /**
     * read file with Text
     *
     * @param path file path
     * @return file content
     * @throws IOException
     */
    public static String readText(String path) throws IOException {
        StringBuilder result = new StringBuilder();
        try {
            if (exist(path)) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(path)));
                while ((line = br.readLine()) != null)
                    result.append(line);
            } else {
                throw new IOException("Cannot find file");
            }

        } catch (IOException e) {
            logger.error("e->" + e.getMessage(), e);
        }
        return result.toString();
    }
}
