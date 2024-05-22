package blacksky.messenger.server.services

import blacksky.messenger.server.exceptions.NotFoundException
import blacksky.messenger.server.exceptions.UnauthorizedException
import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.Message
import blacksky.messenger.server.models.User
import blacksky.messenger.server.models.haveParticipant
import kotlinx.coroutines.*
import java.io.Closeable
import java.util.*
import kotlin.time.Duration.Companion.seconds

data class PostMessageDto(val authorId: UUID, val conversationId: UUID, val text: String)

data class CreateConversationDto(val name: String)

data class AddUserToConversationDto(val userId: UUID, val conversationId: UUID)

data class CreateUserDto(val login: String, val password: String) {
    fun toUser() = User(login, password.hashCode())
}

data class UserDto(val id: UUID, val login: String) {
    constructor(user: User) : this(user.id, user.login)
}

data class MessageDto(val id: UUID, val authorId: UUID, val text: String) {
    constructor(message: Message) : this(message.id, message.author.id, message.text)
}

data class ConversationDto(val id: UUID, val creatorId: UUID, val name: String, val participants: List<UserDto>) {
    constructor(conversation: Conversation) : this(
        conversation.id,
        conversation.creator.id,
        conversation.name,
        conversation.participants.map { UserDto(it) })
}

object DataService : Closeable {
    private val userByLogin = mutableMapOf<String, User>()
    private val conversations = mutableListOf<Conversation>()
    private val userIdByToken = mutableMapOf<UUID, UUID>()

    private const val TOKEN_LIMIT = 10_000
    private val scope = CoroutineScope(Job() + Dispatchers.Default)
    private val housekeepingJob = scope.launch { doHouseKeeping() }
    private const val ADMIN_PASSWORD_HASH = -1846217193

    private fun genToken(user: User): UUID = UUID.randomUUID().apply { userIdByToken[this] = user.id }

    private suspend fun doHouseKeeping() {
        while (true) {
            if (userIdByToken.size > TOKEN_LIMIT) userIdByToken.clear()
            delay(10.seconds)
        }
    }

    fun addUser(password: String, dto: CreateUserDto) =
        if (password.hashCode() == ADMIN_PASSWORD_HASH) UserDto(dto.toUser().also {
            userByLogin[it.login] = it
        }) else throw UnauthorizedException("Bad password")

    fun tryLogin(login: String, password: String) =
        userByLogin[login]?.takeIf { it.passwordHash == password.hashCode() }?.let { genToken(it) }
            ?: throw UnauthorizedException("Login failed")

    fun addConversation(token: UUID, dto: CreateConversationDto) =
        ConversationDto(Conversation(getUserByToken(token), dto.name).apply { conversations.add(this) })


    fun getConversations(token: UUID) = getFullConversations(token).map { ConversationDto(it) }

    fun getMessages(token: UUID, conversationId: UUID) =
        getFullConversations(token).firstOrNull { it.id == conversationId }?.messages?.map { MessageDto(it) }
            ?: throw NotFoundException("No such conversation")

    fun postMessage(token: UUID, dto: PostMessageDto): MessageDto = TODO("Not implemented yet")

    fun addUserToConversation(token: UUID, dto: AddUserToConversationDto): Nothing = TODO("Not implemented yet")

    override fun close() = runBlocking { housekeepingJob.cancelAndJoin() }

    private fun getFullConversations(token: UUID) =
        userIdByToken[token]?.let { userId -> conversations.filter { it.haveParticipant(userId) } }
            ?: throw UnauthorizedException("No such token")

    private fun getUserByToken(token: UUID): User = TODO("Not implemented yet")
}