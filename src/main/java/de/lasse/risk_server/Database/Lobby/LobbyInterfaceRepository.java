package de.lasse.risk_server.Database.Lobby;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface LobbyInterfaceRepository extends MongoRepository<Lobby, String> {

    public List<Lobby> findAll();

    public Lobby findByGameId(String gameId);

}
