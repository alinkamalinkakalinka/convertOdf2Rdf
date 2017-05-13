package utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.atlas.lib.CollectionUtils;
import org.apache.jena.ext.com.google.common.base.Utf8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aarunova on 3/26/17.
 */
public class RegexHelper {

    public static InputStream getDateBetweenTags (InputStream data) throws IOException {

        StringWriter writer = new StringWriter();
        IOUtils.copy(data, writer);
        String dataString = writer.toString();

        String foundData = findMatch("(\\<Objects\\>[\\S\\s]+?\\<\\/Objects\\>)", dataString);
        if (foundData == null) {
            foundData = findMatch("(\\<Objects[\\S\\s]+?\\<\\/Objects\\>)", dataString);
        }

        InputStream is = new ByteArrayInputStream(foundData.getBytes());

        return is;
    }

    private static String findMatch(String pattern, String dataString) {
        ArrayList<String> list = new ArrayList<>();

        Pattern productsPattern = Pattern.compile(pattern);

        Matcher m = productsPattern.matcher(dataString);
        while (m.find()) {
            list.add(m.group(0));
        }

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    public static String getObjectType (String url) {

        ArrayList<String> list = new ArrayList<>();

        Pattern productsPattern = Pattern.compile("(?<=:)[a-zA-Z]+?$");
        Matcher m = productsPattern.matcher(url);
        while (m.find()) {
            list.add(m.group(0));
        }

        return list.get(0);

    }


}
