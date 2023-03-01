package com.example.bin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = BinRepository(application)

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        toastMutableLiveData.postValue("Exception on $coroutineContext: throwable ${throwable.message}")
    }

    private val binMutableLiveData = MutableLiveData<Bin>()
    private val binListMutableLiveData = SingleLiveEvent<List<Bin>>()
    private val toastMutableLiveData = SingleLiveEvent<String>()

    val binLiveData: LiveData<Bin>
        get() = binMutableLiveData
    val toastLiveData: LiveData<String>
        get() = toastMutableLiveData
    val binListLiveData: LiveData<List<Bin>>
        get() = binListMutableLiveData

    fun getBinData(bin: String) {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            binMutableLiveData.postValue(repository.getBinData(bin))
        }
    }

    fun getAllBinFromDatabase() {
        viewModelScope.launch(exceptionHandler + Dispatchers.IO) {
            binListMutableLiveData.postValue(repository.getBinList())
        }
    }

}