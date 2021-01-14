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

    override fun getClientRoleMappingsStream(app: ClientModel?): Stream<RoleModel> {
        logger.info { "Calling getClientRoleMappingsStream" }
        val userRoles = this.user?.roles?.filter { it.client == app?.name } ?: emptyList()
        val clientRoles = mutableSetOf<RoleModel>()
        userRoles.forEach {
            if (app != null) {
                clientRoles.add(app.getRole(it.name))
            }
        }
        return clientRoles.stream()
    }

    override fun getClientRoleMappings(app: ClientModel?): MutableSet<RoleModel> {
        logger.info { "Calling getClientRoleMappings" }
        val userRoles = this.user?.roles?.filter { it.client == app?.name } ?: emptyList()
        val clientRoles = mutableSetOf<RoleModel>()
        userRoles.forEach {
            if (app != null) {
                clientRoles.add(app.getRole(it.name))
            }
        }
        return clientRoles
    }


    override fun getRoleMappings(): MutableSet<RoleModel> {
        val roles = mutableSetOf<RoleModel>()
        this.user?.roles?.forEach {
            if (realm != null) {
                roles.add(realm.getRole(it.name))
            }
        }
        return roles
    }
}