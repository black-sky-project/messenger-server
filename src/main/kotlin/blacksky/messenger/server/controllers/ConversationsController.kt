package blacksky.messenger.server.controllers

import blacksky.messenger.server.dtos.AddUserToConversationDto
import blacksky.messenger.server.dtos.CreateConversationDto
import blacksky.messenger.server.services.DataService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/conversations")
class ConversationsController {
    @GetMapping("/get")
    fun getConversations(@RequestHeader("Token") token: UUID) = DataService.getConversations(token)

    @PostMapping("/new")
    fun postConversation(@RequestHeader("Token") token: UUID, @RequestBody dto: CreateConversationDto) =
        DataService.addConversation(token, dto)

    @PutMapping("/addUser")
    fun addUserToConversation(
        @RequestHeader("Token") token: UUID, @RequestBody dto: AddUserToConversationDto
    ) = DataService.addUserToConversation(token, dto)
}