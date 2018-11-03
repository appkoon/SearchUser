package com.appkoon.searchuser.di.module

import com.appkoon.searchuser.ui.detail.LikeFragment
import com.appkoon.searchuser.ui.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): LikeFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}
