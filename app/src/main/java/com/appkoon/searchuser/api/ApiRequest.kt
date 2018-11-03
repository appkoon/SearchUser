package com.appkoon.searchuser.api

import android.annotation.SuppressLint
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


class ApiRequest {

    companion object {
        @SuppressLint("CheckResult")
        fun <T> request(single: Single<Response<T>>, listener: ApiResponse<T>) {
            single.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ tResponse: Response<T> ->
                                    if (tResponse.isSuccessful) listener.onSuccess(tResponse.body()!!, tResponse.raw())
                                    else listener.onServerError(tResponse.errorBody().toString())
                                }, listener::onError)
        }
    }
}
