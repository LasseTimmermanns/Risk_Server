package de.lasse.risk_server.Lobby;

import de.lasse.risk_server.Database.Maps.DisplayMap;
import javafx.scene.shape.SVGPath;

@Deprecated
public class FlagPosition {

    public static boolean isInside(DisplayMap map, double flagx, double flagy) {
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(map.displayPath);
        // svgPath.setStyle("-fx-transform: perspective(400px) rotateX(45deg);");
        svgPath.setStyle("-fx-transform: translateX(50px) rotate(45deg) scale(1.5);");

        boolean isInside = svgPath.contains(flagx, flagy);
        System.out.println("IsInside: " + isInside);

        return isInside;
    }

    public static double[] generateRandomValidCoordinates(DisplayMap map) {
        double x, y;
        do {
            x = Math.random() * map.width;
            y = Math.random() * map.height;
        } while (!isInside(map, x, y));

        return new double[] { x, y };
    }

}
