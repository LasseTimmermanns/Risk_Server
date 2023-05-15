package de.lasse.risk_server.Utils;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class QueryUtil {

    public static String getQueryValue(String key, String query) {
        query += "&";
        final String regex = key + "=(.*?)(?=&)";

        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(query);

        if (!matcher.find())
            return null;

        return matcher.group(1);
    }

}
