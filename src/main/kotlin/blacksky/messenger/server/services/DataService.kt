package blacksky.messenger.server.services

import blacksky.messenger.server.dtos.*
import blacksky.messenger.server.exceptions.BadRequestException
import blacksky.messenger.server.exceptions.ForbiddenException
import blacksky.messenger.server.exceptions.NotFoundException
import blacksky.messenger.server.exceptions.UnauthorizedException
import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.User
import blacksky.messenger.server.models.haveParticipant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.time.Duration.Companion.seconds

object DataService {
    private val userByLogin = mutableMapOf<String, User>()
    private val conversations = mutableListOf<Conversation>()
    private val userIdByToken = mutableMapOf<UUID, UUID>()

    private const val TOKEN_LIMIT = 10_000
    private const val ADMIN_PASSWORD_HASH = -1846217193

    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch { doHouseKeeping() }
    }

    fun addUser(password: String, dto: CreateUserDto) =
        if (password.hashCode() == ADMIN_PASSWORD_HASH) UserDto(dto.toUser().also {
            userByLogin[it.login] = it
        }) else throw UnauthorizedException("Bad password")

    fun tryLogin(login: String, password: String) =
        userByLogin[login]?.takeIf { it.passwordHash == password.hashCode() }?.let { genToken(it) }
            ?: throw UnauthorizedException("Login failed")

    fun addConversation(token: UUID, dto: CreateConversationDto) =
        ConversationDto(Conversation(getUserByToken(token), dto.name).also { conversations.add(it) })

    fun getConversations(token: UUID) = getFullConversations(token).map { ConversationDto(it) }

    fun getMessages(token: UUID, conversationId: UUID) =
        getFullConversations(token).firstOrNull { it.id == conversationId }?.messages?.map { MessageDto(it) }
            ?: throw NotFoundException("No such conversation")

    fun postMessage(token: UUID, dto: PostMessageDto): MessageDto = TODO("Not implemented yet")

    fun addUserToConversation(token: UUID, dto: AddUserToConversationDto) {
        with(conversations.firstOrNull { it.id == dto.conversationId }
            ?: throw NotFoundException("No such conversation")) {
            val admin = getUserByToken(token)
            if (creator.id != admin.id) throw ForbiddenException("You are not the conversation creator")
            if (haveParticipant(dto.userId)) throw BadRequestException("User is already member")
            val user = userByLogin.values.firstOrNull { it.id == dto.userId } ?: throw NotFoundException("No such user")
            participants.add(user)
        }
    }


    // region PrivateMethods
    private fun genToken(user: User): UUID = UUID.randomUUID().apply { userIdByToken[this] = user.id }

    private fun getFullConversations(token: UUID) =
        userIdByToken[token]?.let { userId -> conversations.filter { it.haveParticipant(userId) } }
            ?: throw UnauthorizedException("No such token")

    private fun getUserByToken(token: UUID): User = userIdByToken[token]?.let { userId ->
        userByLogin.values.firstOrNull { it.id == userId } ?: throw NotFoundException("No such user")
    } ?: throw UnauthorizedException("No such token")

    private suspend fun doHouseKeeping() {
        while (true) {
            if (userIdByToken.size > TOKEN_LIMIT) userIdByToken.clear()
            delay(10.seconds)
        }
    }
    //endregion
}