package de.lasse.risk_server.Lobby;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Database.Maps.DisplayMap;
import de.lasse.risk_server.Database.Maps.DisplayMatrixRepository;
import de.lasse.risk_server.Database.Maps.MapInterfaceRepository;

@Service
public class FlagPosition {

    @Autowired
    DisplayMatrixRepository displayMatrixRepository;

    @Autowired
    MapInterfaceRepository mapInterfaceRepository;

    public boolean isInside(DisplayMap map, double flagx, double flagy) {
        if (flagx > map.width || flagy > map.height)
            return false;
        return this.displayMatrixRepository.getAccessibility(map.id, (int) flagx, (int) flagy);
    }

    public boolean isInside(String mapId, double flagx, double flagy) {
        return this.isInside(this.mapInterfaceRepository.findDisplayMapById(mapId), flagx, flagy);
    }

    public double[] generateRandomValidCoordinates(String mapId) {
        DisplayMap map = this.mapInterfaceRepository.findDisplayMapById(mapId);
        double x, y;
        do {
            x = Math.random() * map.width;
            y = Math.random() * map.height;
        } while (!this.isInside(map, x, y));

        return new double[] { x, y };
    }

}
