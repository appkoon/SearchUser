package com.appkoon.searchuser.ui.search

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.os.Handler
import android.util.Log
import com.appkoon.searchuser.api.ApiRequest
import com.appkoon.searchuser.api.ApiResponse
import com.appkoon.searchuser.api.Error
import com.appkoon.searchuser.api.Status
import com.appkoon.searchuser.repository.Repository
import com.appkoon.searchuser.vo.Document
import com.appkoon.searchuser.vo.Item
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class SearchViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var page = 1
    var query = ""
    val delayTime = 500L
    private var totalCount = 0

    val messageLiveData: LiveData<String> by lazy { MutableLiveData<String>() }
    val responseLiveData: LiveData<List<Item>> by lazy { MutableLiveData<List<Item>>() }

    val status = ObservableField<Status>()
    val dataCount = ObservableInt()
    val errorText = ObservableField<String>()

    private fun reset(query: String) {
        page = 1
        this.query = query
        totalCount = 0
        dataCount.set(0)
    }

    @SuppressLint("CheckResult")
    fun search(query: String, reset: Boolean) {
        setStatus(Status.LOADING)
        if (reset) reset(query)
        if (totalCount > 0 && dataCount.get() == totalCount) {
            (messageLiveData as MutableLiveData<*>).value = Error.NO_MORE_DATA.value
        }else{
            ApiRequest.request(repository.search(query, page), object : ApiResponse<Document>{
                override fun onSuccess(data: Document, response: Response) {
                    totalCount = data.total_count
                    dataCount.set(dataCount.get() + data.items.size)
                    setStatus(Status.SUCCESS)
                    Handler().postDelayed({
                        (responseLiveData as MutableLiveData<*>).value = repository.checkLike(data.items)
                        page++
                    }, delayTime)
                }
                override fun onError(throwable: Throwable) {
                    when (throwable) {
                        is HttpException -> setStatus(Status.ERROR, Error.UNKNOWN.value)
                        is SocketTimeoutException -> setStatus(Status.ERROR, Error.TIMEOUT.value)
                        is IOException -> setStatus(Status.ERROR, Error.DISCONNECTED.value)
                        else -> setStatus(Status.ERROR, Error.UNKNOWN.value)
                    }
                }
                override fun onServerError(errorMessage: String) {
                    setStatus(Status.ERROR, errorMessage)
                }
            })
        }
    }

    fun searchNextPage() {
        search(query, false)
    }

    fun retry() {
        search(query, true)
    }

    private fun setStatus(status: Status, message: String? = null){
        this.status.set(status)
        if (message != null) this.errorText.set(message)
    }

    @SuppressLint("CheckResult")
    fun insert(item: Item) {
        Log.d("good", "item = $item")
        Observable.just(repository)
                  .subscribeOn(Schedulers.io())
                  .subscribe { it.insert(item) }

    }

}