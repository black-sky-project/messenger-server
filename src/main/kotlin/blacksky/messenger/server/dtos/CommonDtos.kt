package blacksky.messenger.server.dtos

import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.Message
import blacksky.messenger.server.models.User
import java.util.*

data class PostMessageDto(val conversationId: UUID, val text: String)

data class CreateConversationDto(val name: String)

data class AddUserToConversationDto(val login: String, val conversationId: UUID)

data class CreateUserDto(val login: String, val password: String) {
    fun toUser() = User(login, password.hashCode())
}

data class UserDto(val id: UUID, val login: String) {
    constructor(user: User) : this(user.id, user.login)
}

data class MessageDto(val id: UUID, val authorId: UUID, val text: String, val date: Date) {
    constructor(message: Message) : this(message.id, message.author.id, message.text, message.date)
}

data class ConversationDto(val id: UUID, val creatorId: UUID, val name: String, val participants: List<UserDto>) {
    constructor(conversation: Conversation) : this(
        conversation.id,
        conversation.creator.id,
        conversation.name,
        conversation.participants.map { UserDto(it) })
}