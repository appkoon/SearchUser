package com.appkoon.searchuser.repository

import android.util.Log
import com.appkoon.searchuser.api.GithubService
import com.appkoon.searchuser.model.dao.ItemDao
import com.appkoon.searchuser.model.vo.Document
import com.appkoon.searchuser.model.vo.Item
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val githubService: GithubService, private val itemDao: ItemDao) {

    fun search(query: String, page: Int) : Single<Response<Document>> = githubService.searchImages(query, page)

    fun checkLike(items: List<Item>) : List<Item> {
        items.forEach { item ->
            itemDao.getItemCountById(item.id)
                   .subscribeOn(Schedulers.io())
                   .subscribe({ (if (it > 0) item.like = true) }, { item.like = false})
        }

        return items
    }

    fun getAllItem() : Flowable<List<Item>> = itemDao.getAllItem()

    fun insert(item: Item) {
        itemDao.insert(item)
    }

    fun delete(item: Item) {
        itemDao.delete(item)
    }

}