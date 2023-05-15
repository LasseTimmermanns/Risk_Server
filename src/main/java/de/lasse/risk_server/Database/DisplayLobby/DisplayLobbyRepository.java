package de.lasse.risk_server.Database.DisplayLobby;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

@Service
public class DisplayLobbyRepository {

        @Autowired
        MongoTemplate mongoTemplate;

        public List<DisplayLobby> getAll() {
                TypedAggregation<DisplayLobby> aggregation = Aggregation.newAggregation(
                                DisplayLobby.class,
                                Aggregation.project()
                                                .and(ArrayOperators.ArrayElemAt.arrayOf("players").elementAt(0))
                                                .as("firstPlayer")
                                                .and(ArrayOperators.Size.lengthOfArray("players")).as("playerCount")
                                                .and("mapId").as("mapId")
                                                .and("cardBonus").as("cardBonus")
                                                .and("turnTimer").as("turnTimer")
                                                .and("maxPlayers").as("maxPlayers"),
                                Aggregation.project()
                                                .and("firstPlayer.name").as("host")
                                                .and("playerCount").as("playerCount")
                                                .and("mapId").as("mapId")
                                                .and("cardBonus").as("cardBonus")
                                                .and("turnTimer").as("turnTimer")
                                                .and("maxPlayers").as("maxPlayers"));

                AggregationResults<DisplayLobby> result = this.mongoTemplate.aggregate(aggregation, "lobbies",
                                DisplayLobby.class);

                return result.getMappedResults();
        }

}
