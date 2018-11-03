package com.appkoon.searchuser.viewmodel

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
import com.appkoon.searchuser.model.dao.ItemDao
import com.appkoon.searchuser.repository.SearchRepository
import com.appkoon.searchuser.model.vo.Document
import com.appkoon.searchuser.model.vo.Item
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
open class SearchViewModel @Inject constructor(private val repository: SearchRepository) : ViewModel() {


    private var page = 1
    var query = ""
    val delayTime = 1000L

    val messageLiveData: LiveData<String> by lazy { MutableLiveData<String>() }
    val responseLiveData: LiveData<List<Item>> by lazy { MutableLiveData<List<Item>>() }

    val status = ObservableField<Status>()
    val dataCount = ObservableInt()
    val errorText = ObservableField<String>()


    @SuppressLint("CheckResult")
    fun search(query: String, reset: Boolean) {
        setStatus(Status.LOADING)
        if (reset) {
            page = 1
            this.query = query
            dataCount.set(0)
        }
        ApiRequest.request(repository.search(query, page), object : ApiResponse<Document>{
            override fun onSuccess(response: Document) {
//                Log.e("good", "page = $page isEnd = ${response.meta.is_end} documents = ${response.documents.size}")
                Log.d("good", "items => ${response.items.size}")
                dataCount.set(response.items.size)
                if (dataCount.get() > 0) {
                    Handler().postDelayed({
                        setStatus(Status.SUCCESS)
                        (responseLiveData as MutableLiveData<*>).value = repository.checkLike(response.items)
                        page++
                    }, delayTime)
                } else {
                    (messageLiveData as MutableLiveData<*>).value = Error.NO_MORE_DATA.value
                }
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