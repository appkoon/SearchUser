package com.appkoon.searchuser.api


interface ApiResponse<T> {
    fun onSuccess(response: T)
    fun onError(throwable: Throwable)
    fun onServerError(errorMessage: String)
}