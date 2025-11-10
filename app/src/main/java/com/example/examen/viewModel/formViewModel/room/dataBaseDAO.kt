package com.example.examen.viewModel.formViewModel.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.examen.viewModel.formViewModel.model.data
import kotlinx.coroutines.flow.Flow

@Dao
interface dataBaseDAO {

    @Query("SELECT * FROM form")
    fun getData(): Flow<List<data>>

    @Query("SELECT * FROM form WHERE id = :id")
    fun getDataById(id: Long): Flow<data>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun update(data: data)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: data)

    @Delete
    suspend fun delete(data: data)
}