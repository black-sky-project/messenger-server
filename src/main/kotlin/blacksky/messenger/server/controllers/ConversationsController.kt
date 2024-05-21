package blacksky.messenger.server.controllers

import blacksky.messenger.server.services.DataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/conversations")
class ConversationsController {
    @GetMapping("/get")
    fun getConversations(@RequestHeader("Token") token: UUID) = DataService.getConversations(token)
}