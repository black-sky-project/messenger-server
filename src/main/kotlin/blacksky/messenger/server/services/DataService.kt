package blacksky.messenger.server.services

import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.Message
import blacksky.messenger.server.models.User
import blacksky.messenger.server.models.haveParticipant
import java.util.*

class DataService {
    private val userByLogin = mutableMapOf<String, User>()
    private val conversations = mutableListOf<Conversation>()
    private val userIdByToken = mutableMapOf<UUID, UUID>()  // TODO: Implement housekeeping

    fun addUser(user: User) = user.also { userByLogin[it.login] = it }

    fun addConversation(conversation: Conversation) = conversations.add(conversation)

    fun tryLogin(login: String, password: String) =
        userByLogin[login]?.takeIf { it.passwordHash == password.hashCode() }?.let { user ->
            with(UUID.randomUUID()) {
                userIdByToken[this] = user.id
                this
            }
        }

    fun getConversations(token: UUID) =
        userIdByToken[token]?.let { userId -> conversations.filter { it.haveParticipant(userId) } }

    fun getMessages(conversationId: UUID) =
        conversations.firstOrNull { it.id == conversationId }?.messages  // TODO: Consider token validation here

    fun addMessage(conversationId: UUID, message: Message) =
        conversations.firstOrNull { it.id == conversationId }?.messages?.add(message)  // TODO: Consider token validation here
}