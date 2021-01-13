package com.nononsensecode.keycloak.application.service

import com.nononsensecode.keycloak.domain.dto.UserDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IUserService {
    @GET("api/v1.0/users/id/{id}")
    fun getUserById(@Path("id") id: String?): Call<UserDTO>

    @GET("api/v1.0/users/username/{username}")
    fun getUserByUsername(@Path("username") username: String?): Call<UserDTO>

    @GET("api/v1.0/users/email/{email}")
    fun getUserByEmail(@Path("email") email: String?): Call<UserDTO>

    @GET("api/v1.0/users/username/{username}/password")
    fun getPasswordById(@Path("username") id: String?): Call<String?>

    @POST("api/v1.0/users/username/{username}")
    fun verifyCredentials(@Body password: String): Call<Boolean>
}