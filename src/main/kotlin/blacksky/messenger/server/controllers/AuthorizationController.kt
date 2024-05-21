package blacksky.messenger.server.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthorizationController {
    @GetMapping("/login/{login}/{password}")  // TODO: Use body here
    fun login(@PathVariable login: String, @PathVariable password: String): String = TODO("Not implemented yet")
}