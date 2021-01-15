package com.nononsensecode.keycloak.domain.model

import com.nononsensecode.keycloak.domain.dto.UserDTO
import mu.KotlinLogging
import org.keycloak.component.ComponentModel
import org.keycloak.models.*
import org.keycloak.storage.adapter.AbstractUserAdapterFederatedStorage
import java.util.stream.Stream

private val logger = KotlinLogging.logger {  }

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

    override fun getRoleMappings(): MutableSet<RoleModel> {
        val roles = addRealmRoles()
        return addClientRoles(roles)
    }

    private fun addRealmRoles(): MutableSet<RoleModel> {
        val roles = mutableSetOf<RoleModel>()
        user?.roles?.forEach { roleDTO ->
            if (realm != null) {
                val realmRole = realm.getRole(roleDTO.name) ?: null
                if (realmRole != null) {
                    roles.add(realmRole)
                }
            }
        }

        return roles
    }

    private fun addClientRoles(roles: MutableSet<RoleModel>): MutableSet<RoleModel> {
        user?.roles?.forEach { roleDTO ->
            realm?.clientsStream?.forEach { client ->
                client.rolesStream.forEach { clientRole ->
                    if (client.clientId == roleDTO.client && clientRole.name == roleDTO.name) {
                        roles.add(clientRole)
                    }
                }
            }
        }

        return roles
    }
}