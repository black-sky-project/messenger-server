package blacksky.messenger.server.models

import java.util.*

data class User(val login: String, val passwordHash: Int) {
    val id: UUID = UUID.randomUUID()
}
