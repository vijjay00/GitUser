package com.initial.gituser.utils

import com.initial.gituser.data.UserDetails
import com.initial.gituser.data.UserRepo
import com.initial.gituser.data.UsersPojo
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MyApi {

    @GET("search/users")
    suspend fun searchUsers(@Query("q") text: String?): Response<UsersPojo>

    @GET("users/{profile}")
    suspend fun getUserDetail(@Path("profile") groupId: String?): Response<UserDetails>

    @GET("users/{profile}/repos")
    suspend fun getUserRepository(@Path("profile") groupId: String?): Response<UserRepo>

    companion object{
        operator fun invoke(): MyApi {

            val okkHttpclient = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}