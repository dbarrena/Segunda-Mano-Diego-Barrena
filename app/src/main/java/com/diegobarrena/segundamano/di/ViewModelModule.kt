package com.diegobarrena.segundamano.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.diegobarrena.segundamano.di.factory.AppViewModelFactory
import com.diegobarrena.segundamano.views.main.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}