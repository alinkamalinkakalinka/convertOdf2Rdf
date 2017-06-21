package utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aarunova on 3/26/17.
 */
public class RegexHelper {

    public static InputStream getDateBetweenTags (String filePath) throws IOException {

        String dataString = FileToStringConverter.getFileAsStringFromFilePath(filePath);

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

    public static String getLiteralValue (String typedLiteral) {

        String literalValue = findMatch("^.+?(?=\\^\\^)", typedLiteral);

        return literalValue;
    }

    public static String getLitralType (String typedLiteral) {

        String literalType = findMatch("(?<=#).+?$", typedLiteral);

        return literalType;
    }

    public static String getOptionalProperty (String property) {

        String optionalProperty = findMatch("(?<=\\/)[^\\/]+?$", property);

        return optionalProperty;
    }

}
