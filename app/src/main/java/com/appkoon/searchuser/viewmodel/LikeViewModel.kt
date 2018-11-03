package com.appkoon.searchuser.viewmodel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appkoon.searchuser.model.vo.Item
import com.appkoon.searchuser.repository.SearchRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@SuppressLint("CheckResult")
class LikeViewModel @Inject constructor(private val repository: SearchRepository): ViewModel() {


    private val items = repository.getAllItem()
    val itemLiveData = MutableLiveData<List<Item>>()


    init{
        items.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe { itemLiveData.value = it }
    }

    fun delete(item: Item) {
        Observable.just(repository)
                .subscribeOn(Schedulers.io())
                .subscribe { it.delete(item) }
    }

}