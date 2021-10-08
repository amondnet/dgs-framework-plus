package com.balancefriends.graphql.dgs.protocols.websockets

/**
 * Is the connection callback called when the
 * client requests the connection initialisation
 * through the message `ConnectionInit`.
 *
 * The message payload (`connectionParams` from the
 * client) is present in the `Context.connectionParams`.
 *
 * - Returning `true` or nothing from the callback will
 * allow the client to connect.
 *
 * - Returning `false` from the callback will
 * terminate the socket by dispatching the
 * close event `4403: Forbidden`.
 *
 * - Returning a `Record` from the callback will
 * allow the client to connect and pass the returned
 * value to the client through the optional `payload`
 * field in the `ConnectionAck` message.
 *
 * Throwing an error from within this function will
 * close the socket with the `Error` message
 * in the close event reason.
 */
interface OnDisconnectConnectListener {
    operator fun invoke(): Any
}