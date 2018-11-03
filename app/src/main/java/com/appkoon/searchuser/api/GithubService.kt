package com.appkoon.searchuser.api

import com.appkoon.searchuser.vo.Document
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface GithubService {

    @GET("search/users")
    fun searchImages(@Query("q") query: String, @Query("page") page: Int): Single<Response<Document>>

}