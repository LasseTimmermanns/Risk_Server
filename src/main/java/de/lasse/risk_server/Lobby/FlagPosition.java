package de.lasse.risk_server.Lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Database.Maps.DisplayMap;
import de.lasse.risk_server.Database.Maps.DisplayMapInterfaceRepository;
import de.lasse.risk_server.Database.Maps.DisplayMatrixRepository;

@Service
public class FlagPosition {

    @Autowired
    DisplayMatrixRepository displayMatrixRepository;

    @Autowired
    DisplayMapInterfaceRepository displayMapInterfaceRepository;

    public boolean isInside(DisplayMap map, double flagx, double flagy) {
        if (flagx > map.display_width || flagy > map.display_height)
            return false;
        return this.displayMatrixRepository.getAccessibility(map.id, (int) flagx, (int) flagy);
    }

    public boolean isInside(String mapId, double flagx, double flagy) {
        return this.isInside(this.displayMapInterfaceRepository.findDisplayMapById(mapId), flagx, flagy);
    }

    public double[] generateRandomValidCoordinates(String mapId) {
        DisplayMap map = this.displayMapInterfaceRepository.findDisplayMapById(mapId);
        return generateRandomValidCoordinates(map);
    }

    public double[] generateRandomValidCoordinates(DisplayMap map) {
        double x, y;
        do {
            x = Math.random() * map.display_width;
            y = Math.random() * map.display_height;
        } while (!this.isInside(map, x, y));

        return new double[] { x, y };
    }

}
