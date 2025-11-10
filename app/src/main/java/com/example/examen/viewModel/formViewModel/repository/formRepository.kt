package com.example.examen.viewModel.formViewModel.repository

import com.example.examen.viewModel.formViewModel.model.data
import javax.inject.Inject
import com.example.examen.viewModel.formViewModel.room.dataBaseDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn

class formRepository @Inject constructor(private val dao: dataBaseDAO) {
    suspend fun addData(data: data) = dao.insert(data)
    suspend fun updateData(data: data) = dao.update(data)
    suspend fun deleteData(data: data) = dao.delete(data)
    fun getData(): Flow<List<data>> = dao.getData().flowOn(Dispatchers.IO).conflate()
    fun getDataById(id: Long): Flow<data> = dao.getDataById(id).flowOn(Dispatchers.IO).conflate()
}