package com.nononsensecode.keycloak.application.impl

import com.nononsensecode.keycloak.application.service.IUserService
import com.nononsensecode.keycloak.domain.dto.PasswordDTO
import com.nononsensecode.keycloak.domain.dto.UserDTO
import com.nononsensecode.keycloak.domain.model.CustomUserModel
import mu.KotlinLogging
import org.keycloak.component.ComponentModel
import org.keycloak.credential.CredentialInput
import org.keycloak.credential.CredentialInputUpdater
import org.keycloak.credential.CredentialInputValidator
import org.keycloak.models.*
import org.keycloak.models.credential.PasswordCredentialModel
import org.keycloak.storage.ReadOnlyException
import org.keycloak.storage.StorageId
import org.keycloak.storage.UserStorageProvider
import org.keycloak.storage.user.UserLookupProvider
import org.keycloak.storage.user.UserQueryProvider
import kotlin.streams.toList

private val logger = KotlinLogging.logger {  }

class RestStorageProvider(
    private val session: KeycloakSession,
    private val componentModel: ComponentModel,
    private val userService: IUserService
): UserStorageProvider, UserLookupProvider, UserQueryProvider,
   CredentialInputUpdater, CredentialInputValidator {
    override fun close() {}

    override fun getUserById(id: String?, realm: RealmModel?): UserModel {
        logger.info { "Getting user by id $id" }
        val externalId = StorageId(id).externalId
        logger.info { "External id: $externalId" }
        val user = userService.getUserByUsername(externalId).execute().body()
        logger.info { "User found: $user" }
        return buildUser(realm, user)
    }

    override fun getUserByUsername(username: String?, realm: RealmModel?): UserModel {
        logger.info { "Finding user with username $username" }
        val user = userService.getUserByUsername(username).execute().body()
        logger.info { "User found: $user" }
        return buildUser(realm, user)
    }

    override fun getUserByEmail(email: String?, realm: RealmModel?): UserModel {
        logger.info { "Finding user with email $email" }
        val user = userService.getUserByEmail(email).execute().body()
        logger.info { "User found: $user" }
        return buildUser(realm, user)
    }

    override fun getUsersCount(realm: RealmModel?): Int {
        logger.info { "Finding total count of users" }
        val count = userService.countUsers().execute().body() ?: 0
        logger.info { "Total count is $count" }
        return count
    }

    override fun getUsers(realm: RealmModel?): MutableList<UserModel> {
        logger.info { "Listing all users" }
        val userList = userService.getAllUsers().execute().body()
        logger.info { "Found ${userList?.size} number of users" }
        return convertUsers(realm, userList)
    }

    override fun getUsers(realm: RealmModel?, firstResult: Int, maxResults: Int): MutableList<UserModel> {
        val userList = userService.getPagedAllUsers(firstResult, maxResults).execute().body()
        return convertUsers(realm, userList)
    }

    override fun searchForUser(search: String?, realm: RealmModel?): MutableList<UserModel> {
        val userList = userService.getUsersBySearchString(search ?: "").execute().body()
        return convertUsers(realm, userList)
    }

    override fun searchForUser(
        search: String?,
        realm: RealmModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        val userList = userService.getPagedUsersBySearchString(search ?: "", firstResult, maxResults)
            .execute().body()
        return convertUsers(realm, userList)
    }

    override fun searchForUser(params: MutableMap<String, String>?, realm: RealmModel?): MutableList<UserModel> {
        return getUsers(realm)
    }

    override fun searchForUser(
        params: MutableMap<String, String>?,
        realm: RealmModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        return getUsers(realm, firstResult, maxResults)
    }

    override fun getGroupMembers(realm: RealmModel?, group: GroupModel?): MutableList<UserModel> {
        return mutableListOf()
    }

    override fun getGroupMembers(
        realm: RealmModel?,
        group: GroupModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        return mutableListOf()
    }

    override fun searchForUserByUserAttribute(
        attrName: String?,
        attrValue: String?,
        realm: RealmModel?
    ): MutableList<UserModel> {
        return mutableListOf()
    }

    private fun convertUsers(realm: RealmModel?, users: List<UserDTO>?): MutableList<UserModel> {
        return users?.map { buildUser(realm, it) }?.toMutableList()
            ?: mutableListOf()
    }

    override fun supportsCredentialType(credentialType: String?): Boolean {
        logger.info { "Checking supports credential type and type is $credentialType" }
        logger.info { "Password credential model type is ${PasswordCredentialModel.TYPE}" }
        return PasswordCredentialModel.TYPE == credentialType
    }

    override fun isConfiguredFor(realm: RealmModel?, user: UserModel?, credentialType: String?): Boolean {
        logger.info { "Checking whether it is configured for password authentication" }
        val isConfigured =  supportsCredentialType(credentialType)
        logger.info { "It is ${if (isConfigured) "" else "not" } configured for password authentication" }
        return isConfigured
    }

    override fun isValid(realm: RealmModel?, user: UserModel?, credentialInput: CredentialInput?): Boolean {
        logger.info { "Checking whether the user password is valid" }
        return if (!supportsCredentialType(credentialInput?.type) || credentialInput !is UserCredentialModel) {
            logger.info { "Password authentication is not possible" }
            false
        } else {
            val password = PasswordDTO(password = credentialInput.challengeResponse)
            val isVerified = userService.verifyCredentials(user?.username ?: "", password)
                .execute().body() ?: false
            logger.info { "Password is ${if (isVerified) "" else "not" } verified" }
            isVerified
        }
    }

    override fun updateCredential(realm: RealmModel?, user: UserModel?, input: CredentialInput?): Boolean {
        if (input?.type == PasswordCredentialModel.TYPE) throw ReadOnlyException("user is read only for this update")

        return false
    }

    override fun disableCredentialType(realm: RealmModel?, user: UserModel?, credentialType: String?) {}

    override fun getDisableableCredentialTypes(realm: RealmModel?, user: UserModel?): MutableSet<String> {
        return mutableSetOf()
    }

    private fun buildUser(realm: RealmModel?, userDTO: UserDTO?): CustomUserModel {
        logger.info { "Building user using DTO: $userDTO" }
        return CustomUserModel(session, realm, componentModel, userDTO)
    }

}