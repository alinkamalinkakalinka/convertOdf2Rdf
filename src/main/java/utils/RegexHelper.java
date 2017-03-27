package utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aarunova on 3/26/17.
 */
public class RegexHelper {

    public static String getObjectName (String url) {

        ArrayList<String> list = new ArrayList<>();

        Pattern productsPattern = Pattern.compile("(?<=/)[a-zA-Z]+?$");
        Matcher m = productsPattern.matcher(url);
        while (m.find()) {
            list.add(m.group(0));
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
