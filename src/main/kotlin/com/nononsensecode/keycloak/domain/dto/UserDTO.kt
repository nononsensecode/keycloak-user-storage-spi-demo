package com.nononsensecode.keycloak.domain.dto

import java.util.*

data class UserDTO(
    val id: UUID,
    val createdAt: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val enabled: Boolean,
    val emailVerified: Boolean
)