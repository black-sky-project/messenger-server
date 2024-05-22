package blacksky.messenger.server.controllers

import blacksky.messenger.server.services.CreateUserDto
import blacksky.messenger.server.services.DataService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthorizationController {
    @PostMapping("/login")
    fun login(@RequestHeader("Login") login: String, @RequestHeader("Password") password: String) =
        DataService.tryLogin(login, password)

    @PostMapping("/new")
    fun newUser(@RequestHeader("Password") password: String, @RequestBody dto: CreateUserDto) =
        DataService.addUser(password, dto)
}