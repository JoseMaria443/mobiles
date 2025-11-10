package com.example.examen.viewModel.formViewModel.di

import android.app.Application
import androidx.room.Room
import com.example.examen.viewModel.formViewModel.room.dataBase
import com.example.examen.viewModel.formViewModel.room.dataBaseDAO
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideDatabase(app: Application): dataBase {
        return Room.databaseBuilder(
            app,
            dataBase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideDataBaseDAO(db: dataBase): dataBaseDAO {
        return db.dataDAO()
    }
}