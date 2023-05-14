package de.lasse.risk_server.Database.DisplayLobby;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;

@Service
public class DisplayLobbyRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<DisplayLobby> getAll() {
        TypedAggregation<DisplayLobby> aggregation = Aggregation.newAggregation(
                DisplayLobby.class,
                Aggregation.project().and("players").size().as("playerCount"));

        AggregationResults<DisplayLobby> result = this.mongoTemplate.aggregate(aggregation, "lobbies",
                DisplayLobby.class);

        return result.getMappedResults();
    }

}
