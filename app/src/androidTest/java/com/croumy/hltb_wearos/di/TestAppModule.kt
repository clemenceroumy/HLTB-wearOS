package com.croumy.hltb_wearos.di

import android.content.Context
import androidx.room.Room
import com.croumy.hltb_wearos.mock.services.MockAppService
import com.croumy.hltb_wearos.mock.services.MockHLTBService
import com.croumy.hltb_wearos.mock.services.MockPreferenceService
import com.croumy.hltb_wearos.presentation.data.AppService
import com.croumy.hltb_wearos.presentation.data.database.AppDatabase
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.di.AppModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
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
    fun provideAppService(): IAppService {
        return MockAppService()
    }

    @Provides
    @Singleton
    fun providePreferencesService(): IPreferenceService {
        return MockPreferenceService()
    }

    @Provides
    @Singleton
    fun provideHLTBService(): IHLTBService {
        return MockHLTBService()
    }
}