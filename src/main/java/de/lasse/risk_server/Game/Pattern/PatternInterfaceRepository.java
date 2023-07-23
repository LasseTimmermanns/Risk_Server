package de.lasse.risk_server.Game.Pattern;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatternInterfaceRepository extends MongoRepository<Pattern, String> {

    public Optional<Pattern> findById(String id);

    public long count();
}
