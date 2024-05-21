package blacksky.messenger.server.services

import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.Message
import blacksky.messenger.server.models.User
import blacksky.messenger.server.models.haveParticipant
import java.util.*

data class PostMessageDto(val authorId: UUID, val conversationId: UUID, val text: String)

object DataService {
    private val userByLogin = mutableMapOf<String, User>()
    private val conversations = mutableListOf<Conversation>()
    private val userIdByToken = mutableMapOf<UUID, UUID>()  // TODO: Implement housekeeping


    fun addUser(user: User) = user.also { userByLogin[it.login] = it }  // TODO: Rework. Debug only

    fun addConversation(conversation: Conversation) = conversations.add(conversation)  // TODO: Rework. Debug only

    fun tryLogin(login: String, password: String) =
        userByLogin[login]?.takeIf { it.passwordHash == password.hashCode() }?.let { genToken(it) }

    fun getConversations(token: UUID) =
        userIdByToken[token]?.let { userId -> conversations.filter { it.haveParticipant(userId) } }

    fun getMessages(token: UUID, conversationId: UUID) =
        getConversations(token)?.firstOrNull { it.id == conversationId }?.messages

    fun postMessage(token: UUID, dto: PostMessageDto): Message = TODO("Not implemented yet")


    private fun genToken(user: User): UUID = UUID.randomUUID().apply { userIdByToken[this] = user.id }
}