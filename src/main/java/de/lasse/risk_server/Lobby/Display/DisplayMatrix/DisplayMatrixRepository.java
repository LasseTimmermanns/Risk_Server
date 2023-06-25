package de.lasse.risk_server.Lobby.Display.DisplayMatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

@Service
public class DisplayMatrixRepository {
        @Autowired
        MongoTemplate mongoTemplate;

        public boolean getAccessibility(String mapId, int x, int y) {
                TypedAggregation<DisplayMatrixAllowed> aggregation = Aggregation.newAggregation(
                                DisplayMatrixAllowed.class,
                                Aggregation.match(Criteria.where("_id").is(mapId)),
                                Aggregation.project()
                                                .and(ArrayOperators.ArrayElemAt
                                                                .arrayOf(ArrayOperators.ArrayElemAt
                                                                                .arrayOf("display_matrix").elementAt(y))
                                                                .elementAt(x))
                                                .as("value"));
                AggregationResults<DisplayMatrixAllowed> result = this.mongoTemplate.aggregate(aggregation, "maps",
                                DisplayMatrixAllowed.class);

                return result.getUniqueMappedResult().value;
        }
}
