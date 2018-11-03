package com.appkoon.searchuser.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.appkoon.searchuser.model.dao.ItemDao
import com.appkoon.searchuser.model.vo.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getItemDao() : ItemDao
}