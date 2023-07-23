package de.lasse.risk_server.Game.Shape;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ShapeInterfaceRepository extends MongoRepository<Shape, String> {

    Optional<Shape> findByPos(int pos);

    @Query(value = "{}", fields = "{'_id': 0, 'pos': 1, 'path': 1}")
    List<Shape> findLimited(Pageable pageable);
}
