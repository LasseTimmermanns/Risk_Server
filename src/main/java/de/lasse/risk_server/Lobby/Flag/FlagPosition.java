package de.lasse.risk_server.Lobby.Flag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.lasse.risk_server.Lobby.Display.DisplayMap.DisplayMap;
import de.lasse.risk_server.Lobby.Display.DisplayMap.DisplayMapInterfaceRepository;
import de.lasse.risk_server.Lobby.Display.DisplayMatrix.DisplayMatrixRepository;

@Service
public class FlagPosition {

    @Autowired
    DisplayMatrixRepository displayMatrixRepository;

    @Autowired
    DisplayMapInterfaceRepository displayMapInterfaceRepository;

    public boolean isInside(DisplayMap map, double flagx, double flagy) {
        if (flagx > map.getDisplayWidth() || flagy > map.getDisplayHeight())
            return false;
        return this.displayMatrixRepository.getAccessibility(map.getId(), (int) flagx, (int) flagy);
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
            x = Math.random() * map.getDisplayWidth();
            y = Math.random() * map.getDisplayHeight();
        } while (!this.isInside(map, x, y));

        return new double[] { x, y };
    }

}
