package blacksky.messenger.server.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.services.DataService
import java.util.*


@RestController
@RequestMapping("/api/v1/conversations")
class ConversationsController {
    @GetMapping("/get/{token}")
    fun getConversations(@PathVariable token: UUID) = DataService.getConversations(token)
}