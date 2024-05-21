package blacksky.messenger.server.models

import java.util.*

data class Conversation(val name: String) {
    val id: UUID = UUID.randomUUID()
    val participants = mutableSetOf<User>()
    val messages = mutableListOf<Message>()
}

fun Conversation.haveParticipant(userId: UUID) = participants.any { it.id == userId }