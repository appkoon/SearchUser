package com.appkoon.searchuser.model.dao

import android.arch.persistence.room.*
import com.appkoon.searchuser.model.vo.Item
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getAllItem(): Flowable<List<Item>>

    @Query("SELECT COUNT(*) FROM item WHERE id = :id")
    fun getItemCountById(id: Int) : Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg item: Item)

    @Delete
    fun delete(vararg item: Item)
}