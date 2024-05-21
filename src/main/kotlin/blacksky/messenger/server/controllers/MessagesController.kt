package blacksky.messenger.server.controllers

import blacksky.messenger.server.models.Message
import blacksky.messenger.server.services.DataService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/messages")
class MessagesController {
    @GetMapping("/get/{token}/{conversationId}")
    fun getMessages(@PathVariable token: UUID, @PathVariable conversationId: UUID) =
        DataService.getMessages(token, conversationId)

    @GetMapping("/new/{token}/{conversationId}")  // TODO: Use post dto here
    fun postMessage(@PathVariable token: UUID, @PathVariable conversationId: UUID): List<Message> = TODO("Not implemented yet")
}