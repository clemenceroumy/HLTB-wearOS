package com.croumy.hltb_wearos.presentation.di

import android.content.Context
import androidx.room.Room
import com.croumy.hltb_wearos.presentation.MainActivity
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.HLTBService
import com.croumy.hltb_wearos.presentation.data.PreferencesService
import com.croumy.hltb_wearos.presentation.data.database.AppDatabase
import com.croumy.hltb_wearos.presentation.data.database.dao.LogDao
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.workers.WorkerHelper
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
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
        .build()

    @Singleton
    @Provides
    fun provideLogsDao(db: AppDatabase) = db.logDao()

    @Provides
    @Singleton
    fun provideAppService(
        logDao: LogDao,
        preferencesService: PreferencesService
    ): IAppService {
        return AppService(logDao, preferencesService)
    }

    @Provides
    @Singleton
    fun providePreferencesService(): IPreferenceService {
        return PreferencesService(MainActivity.context)
    }

    @Provides
    @Singleton
    fun provideHLTBService(preferencesService: PreferencesService): IHLTBService {
        return HLTBService(preferencesService)
    }

    @Provides
    @Singleton
    fun provideWorkerHelper(): IWorkerHelper {
        return WorkerHelper()
    }
}