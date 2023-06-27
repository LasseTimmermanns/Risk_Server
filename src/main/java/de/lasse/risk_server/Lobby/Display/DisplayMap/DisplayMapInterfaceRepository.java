package de.lasse.risk_server.Lobby.Display.DisplayMap;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import de.lasse.risk_server.Lobby.Display.DisplayMap.MapInterfaces.BackgroundMap;
import de.lasse.risk_server.Lobby.Display.DisplayMap.MapInterfaces.MiniatureMap;

public interface DisplayMapInterfaceRepository extends MongoRepository<DisplayMap, String> {

    public DisplayMap findDisplayMapById(String id);

    public List<DisplayMap> findAll();

    @Query(value = "{'_id': ?0}", fields = "{ '_id': 1, 'display_width': 1, 'display_height': 1 }")
    public BackgroundMap findBackgroundMapById(String id);

    @Query(value = "{}", fields = "{ '_id': 1, 'svg_width': 1, 'svg_height': 1, 'display_path': 1 }")
    public List<MiniatureMap> findMiniatureMaps();

}
