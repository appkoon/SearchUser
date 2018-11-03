package com.appkoon.searchuser.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.appkoon.searchuser.viewmodel.AppViewModelFactory
import com.appkoon.searchuser.di.ViewModelKey
import com.appkoon.searchuser.viewmodel.LikeViewModel
import com.appkoon.searchuser.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LikeViewModel::class)
    abstract fun bindLikeViewModel(likeViewModel: LikeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
