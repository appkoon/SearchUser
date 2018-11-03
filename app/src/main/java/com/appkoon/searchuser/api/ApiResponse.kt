package com.appkoon.searchuser.api

import okhttp3.Response


interface ApiResponse<T> {
    fun onSuccess(data: T, response: Response)
    fun onError(throwable: Throwable)
    fun onServerError(errorMessage: String)
}