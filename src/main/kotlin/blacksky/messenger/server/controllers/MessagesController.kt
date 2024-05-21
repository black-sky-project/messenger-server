package blacksky.messenger.server.controllers

import blacksky.messenger.server.services.DataService
import blacksky.messenger.server.services.PostMessageDto
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/messages")
class MessagesController {
    @GetMapping("/get/{conversationId}")
    fun getMessages(@RequestHeader("Token") token: UUID, @PathVariable conversationId: UUID) =
        DataService.getMessages(token, conversationId)

    @PostMapping("/new")
    fun postMessage(@RequestHeader("Token") token: UUID, @RequestBody dto: PostMessageDto) =
        DataService.postMessage(token, dto)
}