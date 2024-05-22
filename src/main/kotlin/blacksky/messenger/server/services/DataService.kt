package blacksky.messenger.server.services

import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.Message
import blacksky.messenger.server.models.User
import blacksky.messenger.server.models.haveParticipant
import kotlinx.coroutines.*
import java.io.Closeable
import java.util.*
import kotlin.time.Duration.Companion.seconds

data class PostMessageDto(val authorId: UUID, val conversationId: UUID, val text: String)

object DataService : Closeable {
    private val userByLogin = mutableMapOf<String, User>()
    private val conversations = mutableListOf<Conversation>()
    private val userIdByToken = mutableMapOf<UUID, UUID>()
    private const val TOKEN_LIMIT = 10_000
    private val scope = CoroutineScope(Job() + Dispatchers.Default)
    private val housekeepingJob = scope.launch { doHouseKeeping() }

    private fun genToken(user: User): UUID = UUID.randomUUID().apply { userIdByToken[this] = user.id }

    private suspend fun doHouseKeeping() {
        while (true) {
            if (userIdByToken.size > TOKEN_LIMIT) userIdByToken.clear()
            delay(10.seconds)
        }
    }

    fun addUser(user: User) = user.also { userByLogin[it.login] = it }  // TODO: Rework. Debug only

    fun addConversation(conversation: Conversation) = conversations.add(conversation)  // TODO: Rework. Debug only

    fun tryLogin(login: String, password: String) =
        userByLogin[login]?.takeIf { it.passwordHash == password.hashCode() }?.let { genToken(it) }

    fun getConversations(token: UUID) =
        userIdByToken[token]?.let { userId -> conversations.filter { it.haveParticipant(userId) } }

    fun getMessages(token: UUID, conversationId: UUID) =
        getConversations(token)?.firstOrNull { it.id == conversationId }?.messages

    fun postMessage(token: UUID, dto: PostMessageDto): Message = TODO("Not implemented yet")

    override fun close() = runBlocking { housekeepingJob.cancelAndJoin() }
}