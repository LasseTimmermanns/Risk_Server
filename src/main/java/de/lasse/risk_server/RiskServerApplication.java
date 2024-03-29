package de.lasse.risk_server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import de.lasse.risk_server.Game.Game.GameInterfaceRepository;
import de.lasse.risk_server.Lobby.Lobby.LobbyInterfaceRepository;

@SpringBootApplication
public class RiskServerApplication {

	@Autowired
	LobbyInterfaceRepository lobbyInterfaceRepository;

	@Autowired
	GameInterfaceRepository gameInterfaceRepository;

	public static void main(String[] args) {
		SpringApplication.run(RiskServerApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void clearTemporaryCollections() {
		lobbyInterfaceRepository.deleteAll();
		// gameInterfaceRepository.deleteAll();
	}

}
