package blacksky.messenger.server

import blacksky.messenger.server.models.Conversation
import blacksky.messenger.server.models.User
import blacksky.messenger.server.services.DataService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessengerServerApplication

fun main(args: Array<String>) {
    runApplication<MessengerServerApplication>(*args)

    val amogus = User("amogus", "amoguspwd".hashCode())
    val dudka = User("dudka", "dudkapwd".hashCode())
    val sufler = User("sufler", "suflerpwd".hashCode())
    with(DataService) {
        addUser(amogus)
        addUser(dudka)
        addUser(sufler)
        addConversation(Conversation("Amogus - Dudka").apply { participants.add(amogus) }
            .apply { participants.add(dudka) })
        addConversation(Conversation("Amogus - Sufler").apply { participants.add(amogus) }
            .apply { participants.add(sufler) })
        addConversation(Conversation("NSU").apply { participants.add(amogus) }.apply { participants.add(dudka) }
            .apply { participants.add(sufler) })
    }
}
