package com.appkoon.searchuser.ui.like

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.appkoon.searchuser.vo.Item
import com.appkoon.searchuser.repository.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@SuppressLint("CheckResult")
class LikeViewModel @Inject constructor(private val repository: Repository): ViewModel() {

    private val itemsObservable = repository.getAll()
    val items = ObservableField<List<Item>>(listOf())

    init{
        itemsObservable.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe { datas ->
                 datas.forEach { it.like = false }
                 items.set(datas)
             }
    }

    fun delete(item: Item) {
        Observable.just(repository)
                .subscribeOn(Schedulers.io())
                .subscribe { it.delete(item) }
    }

}