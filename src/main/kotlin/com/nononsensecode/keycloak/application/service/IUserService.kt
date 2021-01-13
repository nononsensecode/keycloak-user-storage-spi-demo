package com.nononsensecode.keycloak.application.service

import com.nononsensecode.keycloak.domain.dto.UserDTO
import retrofit2.Call
import retrofit2.http.*

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

    @GET("api/v1.0/users/count")
    fun countUsers(): Call<Int>

    @GET("api/v1.0/users")
    fun getAllUsers(): Call<List<UserDTO>>

    @GET("api/v1.0/users")
    fun getPagedAllUsers(@Query("start") start: Int, @Query("maxResults") maxResults: Int): Call<List<UserDTO>>

    @GET("api/v1.0/users/search/{searchString}")
    fun getUsersBySearchString(@Path("searchString") searchString: String): Call<List<UserDTO>>

    @GET("api/v1.0/users/search/{searchString}")
    fun getPagedUsersBySearchString(@Path("searchString") searchString: String, @Query("start") start: Int, @Query("maxResults") maxResults: Int): Call<List<UserDTO>>
}