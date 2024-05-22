package blacksky.messenger.server.models

import java.util.*

data class Conversation(val creator: User, val name: String) {
    val id: UUID = UUID.randomUUID()
    val participants = mutableSetOf(creator)
    val messages = mutableListOf<Message>()
}

fun Conversation.haveParticipant(userId: UUID) = participants.any { it.id == userId }