package com.balancefriends.graphql.dgs.protocols.websockets

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import graphql.ExecutionResult
import graphql.GraphQLError


object MessageType {
    // Message types
    const val CONNECTION_INIT = "connection_init"
    const val CONNECTION_ACK = "connection_ack"
    const val PING = "ping"
    const val PONG = "pong"
    const val SUBSCRIBE = "subscribe"
    const val NEXT = "next"
    const val ERROR = "error"
    const val COMPLETE = "complete"
}

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CUSTOM,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(Message.ConnectionInitMessage::class, name = MessageType.CONNECTION_INIT),
    JsonSubTypes.Type(Message.ConnectionAckMessage::class, name = MessageType.CONNECTION_ACK),
    JsonSubTypes.Type(Message.PingMessage::class, name = MessageType.PING),
    JsonSubTypes.Type(Message.PongMessage::class, name = MessageType.PONG),
    JsonSubTypes.Type(Message.SubscribeMessage::class, name = MessageType.SUBSCRIBE),
    JsonSubTypes.Type(Message.NextMessage::class, name = MessageType.NEXT),
    JsonSubTypes.Type(Message.ErrorMessage::class, name = MessageType.ERROR),
    JsonSubTypes.Type(Message.CompleteMessage::class, name = MessageType.COMPLETE),
    )
sealed class Message(
    @JsonProperty("type")
    val type: String
) {
    data class ConnectionInitMessage(val payload: Map<String, Any>? = null) :
        Message(MessageType.CONNECTION_INIT)

    data class ConnectionAckMessage(val payload: Map<String, Any>? = null) :
        Message(MessageType.CONNECTION_ACK)

    data class PingMessage(val payload: Map<String, Any>? = null) : Message(MessageType.PING)

    data class PongMessage(val payload: Map<String, Any>? = null) : Message(MessageType.PONG)

    data class SubscribeMessage(
        val id: String, val payload: Payload,
    ) : Message(MessageType.SUBSCRIBE) {
        data class Payload(
            val operationName: String? = null,
            val query: String,
            val variables: Map<String, Any>? = null,
            val extensions: Map<String, Any>? = null,
        )
    }

    data class NextMessage(
        val id: String, val payload: ExecutionResult,
    ) : Message(MessageType.PONG)

    data class ErrorMessage(
        val id: String, val payload: List<GraphQLError>
    ) : Message(MessageType.ERROR)

    data class CompleteMessage(
        val id: String
    ) : Message(MessageType.COMPLETE)
}

