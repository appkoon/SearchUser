package com.appkoon.searchuser.di.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.appkoon.searchuser.database.AppDataBase
import com.appkoon.searchuser.database.ItemDao
import com.appkoon.searchuser.common.ActionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideContext(app: Application): Context {
        return app
    }

    @Singleton
    @Provides
    fun provideActionManager(): ActionManager = ActionManager.instance

    @Singleton
    @Provides
    fun provideDataBase(app: Application): AppDataBase = Room.databaseBuilder(app, AppDataBase::class.java, "database.db").build()

    @Singleton
    @Provides
    fun provideItemDao(database: AppDataBase): ItemDao = database.getItemDao()
}