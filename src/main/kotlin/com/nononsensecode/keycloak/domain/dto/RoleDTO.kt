package com.nononsensecode.keycloak.domain.dto

data class RoleDTO(
    val id: Long,
    val name: String,
    val client: String
)
