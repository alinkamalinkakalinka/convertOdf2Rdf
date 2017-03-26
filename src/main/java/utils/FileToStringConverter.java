package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aarunova on 3/14/17.
 */
public class FileToStringConverter {

    private static final Logger LOG = LoggerFactory.getLogger(FileToStringConverter.class);

    public static String getFileAsStringFromResources(String fileName) {

        String fileAsString = "";
        InputStream inputStream = FileToStringConverter.class.getResourceAsStream(fileName);
        fileAsString = getFileAsString(inputStream);

        return fileAsString;
    }

    public static String getFileAsStringFromFilePath(String filePath) {

        String fileAsString = "";
        try {
            InputStream inputStream = new FileInputStream(filePath);
            fileAsString = getFileAsString(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileAsString;

    }

    private static String getFileAsString(InputStream file) {
        String fileAsString = "";

        try {
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(file));
            StringBuilder sb = new StringBuilder();
            String line = bufReader.readLine();

            while (line != null) {
                sb.append(line).append("\n");
                line = bufReader.readLine();
            }

            fileAsString = sb.toString();
        } catch (IOException e) {
            LOG.warn("IOException", e);
        }
        return fileAsString;
    }
}
