package blacksky.messenger.server.controllers

import blacksky.messenger.server.services.DataService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthorizationController {
    @PostMapping("/login")
    fun login(@RequestHeader("Login") login: String, @RequestHeader("Password") password: String) =
        DataService.tryLogin(login, password)
}