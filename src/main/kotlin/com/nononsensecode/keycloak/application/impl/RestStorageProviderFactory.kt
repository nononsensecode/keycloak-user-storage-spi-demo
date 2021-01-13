package com.nononsensecode.keycloak.application.impl

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nononsensecode.keycloak.application.service.IUserService
import org.keycloak.Config
import org.keycloak.component.ComponentModel
import org.keycloak.models.KeycloakSession
import org.keycloak.storage.UserStorageProviderFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

class RestStorageProviderFactory: UserStorageProviderFactory<RestStorageProvider> {

    companion object {
        @JvmStatic
        val PROVIDER_ID = "rest-storage-provider"
    }

    private lateinit var userService: IUserService

    override fun init(config: Config.Scope?) {
        val mapper = jacksonObjectMapper()
        val retrofit = Retrofit.Builder()
            .baseUrl(config?.get("api") ?: throw NoSuchFieldError("There is no config property with the name 'api'"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()
        userService = retrofit.create(IUserService::class.java)
    }

    override fun getId(): String {
        return PROVIDER_ID
    }

    override fun create(session: KeycloakSession, model: ComponentModel): RestStorageProvider {
        return RestStorageProvider(session, model, userService)
    }
}