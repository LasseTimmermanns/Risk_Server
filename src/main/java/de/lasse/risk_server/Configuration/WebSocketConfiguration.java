package de.lasse.risk_server.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import de.lasse.risk_server.Game.GameHandler;
import de.lasse.risk_server.Lobby.LobbyHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(lobbyWebSocketHandler(), "/lobby").setAllowedOrigins("*");
        registry.addHandler(gameWebSocketHandler(), "/game").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler lobbyWebSocketHandler() {
        return new LobbyHandler();
    }

    @Bean
    public WebSocketHandler gameWebSocketHandler() {
        return new GameHandler();
    }

}
