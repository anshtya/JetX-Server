package com.anshtya.jetx.server.exception

class NotFoundException(message: String): RuntimeException(message)
class UsernameAlreadyExistsException(): RuntimeException()