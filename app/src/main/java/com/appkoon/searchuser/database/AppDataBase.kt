package com.appkoon.searchuser.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.appkoon.searchuser.vo.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getItemDao() : ItemDao
}