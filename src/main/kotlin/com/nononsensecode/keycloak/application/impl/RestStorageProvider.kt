package com.nononsensecode.keycloak.application.impl

import com.nononsensecode.keycloak.application.service.IUserService
import com.nononsensecode.keycloak.domain.model.CustomUserModel
import org.keycloak.component.ComponentModel
import org.keycloak.models.GroupModel
import org.keycloak.models.KeycloakSession
import org.keycloak.models.RealmModel
import org.keycloak.models.UserModel
import org.keycloak.storage.StorageId
import org.keycloak.storage.UserStorageProvider
import org.keycloak.storage.user.UserLookupProvider
import org.keycloak.storage.user.UserQueryProvider

class RestStorageProvider(
    private val session: KeycloakSession,
    private val componentModel: ComponentModel,
    private val userService: IUserService
): UserStorageProvider, UserLookupProvider, UserQueryProvider {
    override fun close() {}

    override fun getUserById(id: String, realm: RealmModel): UserModel {
        val externalId = StorageId.externalId(id)
        val user = userService.getUserById(externalId).execute().body()
        return CustomUserModel(session, realm, componentModel, user)
    }

    override fun getUserByUsername(username: String, realm: RealmModel): UserModel {
        val user = userService.getUserByUsername(username).execute().body()
        return CustomUserModel(session, realm, componentModel, user)
    }

    override fun getUserByEmail(email: String, realm: RealmModel): UserModel {
        val user = userService.getUserByEmail(email).execute().body()
        return CustomUserModel(session, realm, componentModel, user)
    }

    override fun getUsersCount(realm: RealmModel?): Int {
        TODO("Not yet implemented")
    }

    override fun getUsers(realm: RealmModel?): MutableList<UserModel> {
        TODO("Not yet implemented")
    }

    override fun getUsers(realm: RealmModel?, firstResult: Int, maxResults: Int): MutableList<UserModel> {
        TODO("Not yet implemented")
    }

    override fun searchForUser(search: String?, realm: RealmModel?): MutableList<UserModel> {
        TODO("Not yet implemented")
    }

    override fun searchForUser(
        search: String?,
        realm: RealmModel?,
        firstResult: Int,
        maxResults: Int
    ): MutableList<UserModel> {
        TODO("Not yet implemented")
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

}