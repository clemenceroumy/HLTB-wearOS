package com.croumy.hltb_wearos.presentation.di

import android.content.Context
import androidx.room.Room
import com.croumy.hltb_wearos.presentation.data.database.AppDatabase
import com.croumy.hltb_wearos.presentation.data.database.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "hltbWear.db"
    )
        .allowMainThreadQueries()
        .build()

    @Singleton
    @Provides
    fun provideLogsDao(db: AppDatabase) = db.logDao()
}