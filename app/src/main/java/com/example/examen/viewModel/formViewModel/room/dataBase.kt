package com.example.examen.viewModel.formViewModel.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.examen.viewModel.formViewModel.room.dataBaseDAO
import com.example.examen.viewModel.formViewModel.model.data

@Database(entities = [data::class], version = 1, exportSchema = false)
abstract class dataBase: RoomDatabase() {
    abstract fun dataDAO(): dataBaseDAO
}