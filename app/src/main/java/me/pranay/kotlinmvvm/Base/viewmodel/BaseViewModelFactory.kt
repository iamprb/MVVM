package me.pranay.kotlinmvvm.Base.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class BaseViewModelFactory : ViewModelProvider.Factory {


    private lateinit var  creators: Map<Class<out ViewModel>, Provider<ViewModel>>

    @Inject
    fun BaseViewModelFactory(creators: Map<Class<out ViewModel>, Provider<ViewModel>>) {
        this.creators = creators
    }
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<out ViewModel>? = creators.get(modelClass)
        if (creator == null) {
            for (entry in creators.entries) {
                if (modelClass.isAssignableFrom(entry.key)) {
                    creator = entry.value
                    break
                }
            }
        }
        if (creator == null) {
            throw IllegalArgumentException("unknown model class $modelClass")
        }
        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}