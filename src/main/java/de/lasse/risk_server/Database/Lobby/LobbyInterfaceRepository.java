package de.lasse.risk_server.Database.Lobby;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LobbyInterfaceRepository extends MongoRepository<Lobby, String> {

    public List<Lobby> findAll();

    public Optional<Lobby> findById(String id);

}
