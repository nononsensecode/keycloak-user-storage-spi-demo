package com.nononsensecode.keycloak.domain.model

import com.nononsensecode.keycloak.domain.dto.UserDTO
import org.keycloak.component.ComponentModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage

class CustomUserModel(
    val session: KeycloakSession,
    val realm: RealmModel?,
    val componentModel: ComponentModel,
    val user: UserDTO?
): AbstractUserAdapterFederatedStorage(session, realm, componentModel), UserModel {
    init {
        firstName = user?.firstName
        lastName = user?.lastName
        email = user?.email
        isEnabled = user?.enabled ?: false
        isEmailVerified = user?.emailVerified ?: false
        createdTimestamp = user?.createdAt
    }

    override fun getUsername(): String? {
        return user?.username
    }

    override fun setUsername(username: String?) {}

}