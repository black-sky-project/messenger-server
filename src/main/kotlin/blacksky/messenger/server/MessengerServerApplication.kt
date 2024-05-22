package blacksky.messenger.server

import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.Message
import blacksky.messenger.server.models.User
import blacksky.messenger.server.services.DataService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessengerServerApplication

fun main(args: Array<String>) {
    runApplication<MessengerServerApplication>(*args)
}
