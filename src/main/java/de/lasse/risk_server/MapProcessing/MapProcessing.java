package de.lasse.risk_server.MapProcessing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapProcessing {

    public static Map createMapObject(String mapName) {
        String mapSvg = getResourceFileAsString("static/maps/" + mapName + ".svg");
        Territory[] territories = createTerritoriesFromString(mapSvg);
        String bordersJson = getResourceFileAsString("static/borders/" + mapName + ".json");
        JSONArray borders = BorderProcessing.getBorders(bordersJson, territories);
        int[] dimensions = getDimensionsFromString(mapSvg);

        return new Map(territories, borders, dimensions[0], dimensions[1]);
    }

    public static String getResourceFileAsString(String fileName) {
        InputStream is = getResourceFileAsInputStream(fileName);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return (String) reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            throw new RuntimeException("resource not found");
        }
    }

    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = MapProcessing.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    public static Territory[] createTerritoriesFromString(String mapSvg) {
        // https://regex101.com/r/NZN4fl/1
        final String regex = "<path id=\"([^\"]*)\" .+?(?=d=\")d=\"([^\"]*)\"";

        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(mapSvg);

        ArrayList<Territory> territories = new ArrayList<Territory>();

        int i = 0;

        while (matcher.find()) {
            String name = matcher.group(1);
            String path = matcher.group(2);

            territories.add(new Territory(i, name, path));
            i++;
        }

        Territory[] terries = new Territory[territories.size()];
        return territories.toArray(terries);
    }

    public static int[] getDimensionsFromString(String mapSvg) {
        final String regex = "width=\"([^\"]*)\" height=\"([^\"]*)\"";
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(mapSvg);

        int[] out = new int[2];

        if (matcher.find()) {
            out[0] = Integer.valueOf(matcher.group(1));
            out[1] = Integer.valueOf(matcher.group(2));
        }
        return out;
    }

}
