package de.lasse.risk_server.Game.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MapRepository {

        @Autowired
        MongoTemplate mongoTemplate;

}
