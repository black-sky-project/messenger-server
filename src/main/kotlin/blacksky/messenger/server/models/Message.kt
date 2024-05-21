package blacksky.messenger.server.models

import java.util.*

data class Message(val author: User, val text: String) {
    val id: UUID = UUID.randomUUID()
}
