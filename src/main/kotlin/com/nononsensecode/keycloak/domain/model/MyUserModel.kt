package com.nononsensecode.keycloak.domain.model

import org.keycloak.component.ComponentModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.storage.adapter.AbstractUserAdapter

class MyUserModel(
    private val session: KeycloakSession?,
    private val realm: RealmModel?,
    private val storageProviderModel: ComponentModel?,
    private var id: String,
    private var createdTimestamp: Long,
    private var username: String,
    private var firstName: String?,
    private var lastName: String?,
    private var email: String?,
    private var enabled: Boolean,
    private var emailVerified: Boolean
): AbstractUserAdapter(session, realm, storageProviderModel) {
    override fun getId() = id
    override fun getUsername() = username
    override fun getFirstName() = firstName
    override fun getLastName() = lastName
    override fun getEmail() = email
    override fun isEnabled() = enabled
    override fun getCreatedTimestamp() = createdTimestamp
    override fun isEmailVerified() = emailVerified

    override fun setUsername(username: String) {
        this.username = username
    }

    override fun setFirstName(firstName: String?) {
        this.firstName = firstName
    }

    override fun setLastName(lastName: String?) {
        this.lastName = lastName
    }

    override fun setEmail(email: String?) {
        this.email = email
    }

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun setEmailVerified(verified: Boolean) {
        this.emailVerified = verified
    }

}