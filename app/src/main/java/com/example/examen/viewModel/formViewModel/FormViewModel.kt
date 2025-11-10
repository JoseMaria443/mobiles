package com.example.examen.viewModel.formViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examen.viewModel.formViewModel.model.data
import com.example.examen.viewModel.formViewModel.repository.formRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(private val repository: formRepository): ViewModel() {
    private var _list = MutableStateFlow<List<data>>(emptyList())
    val list = _list.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getData().collect { item ->
                if (item.isNullOrEmpty()){
                    _list.value = emptyList()
                    return@collect
                }else{
                    _list.value = item
                }
            }
        }
    }

    fun add(data: data) = viewModelScope.launch { repository.addData(data) }
    fun update(data: data) = viewModelScope.launch { repository.updateData(data) }
    fun delete(data: data) = viewModelScope.launch { repository.deleteData(data) }
}