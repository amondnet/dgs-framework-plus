package com.balancefriends.graphql.dgs.protocols.websockets

import com.netflix.graphql.dgs.DgsQueryExecutor
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.support.DefaultHandshakeHandler

@Configuration
@ConditionalOnWebApplication
open class DgsWebsSocketProtocolAutoConfig {
    open fun webSocketHandler(@Suppress("SpringJavaInjectionPointsAutowiringInspection") dgsQueryExecutor: DgsQueryExecutor): WebSocketHandler {
        return DgsWebsocketTransport(dgsQueryExecutor)
    }

    @Configuration
    @EnableWebSocket
    internal open class WebSocketConfig(@Suppress("SpringJavaInjectionPointsAutowiringInspection") private val webSocketHandler: WebSocketHandler) :
        WebSocketConfigurer {

        override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
            val defaultHandshakeHandler = DefaultHandshakeHandler()
            defaultHandshakeHandler.setSupportedProtocols("graphql-transport-ws")
            registry.addHandler(webSocketHandler, "/graphql").setHandshakeHandler(defaultHandshakeHandler).setAllowedOrigins("*")
        }
    }
}