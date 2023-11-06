package com.croumy.hltb_wearos.di

import android.content.Context
import androidx.room.Room
import com.croumy.hltb_wearos.mock.services.MockAppService
import com.croumy.hltb_wearos.mock.services.MockHLTBService
import com.croumy.hltb_wearos.mock.services.MockPreferenceService
import com.croumy.hltb_wearos.mock.workers.SaveTimeWorkerFactory
import com.croumy.hltb_wearos.mock.workers.helpers.BindWorkerToFactory
import com.croumy.hltb_wearos.mock.workers.helpers.TestWorkHelper
import com.croumy.hltb_wearos.presentation.data.database.AppDatabase
import com.croumy.hltb_wearos.presentation.data.interfaces.IAppService
import com.croumy.hltb_wearos.presentation.data.interfaces.IHLTBService
import com.croumy.hltb_wearos.presentation.data.interfaces.IPreferenceService
import com.croumy.hltb_wearos.presentation.di.AppModule
import com.croumy.hltb_wearos.presentation.workers.interfaces.IWorkerHelper
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

    @Provides
    @Singleton
    fun provideWorkerHelper(bindWorkerToFactory: BindWorkerToFactory): IWorkerHelper {
        return TestWorkHelper(bindWorkerToFactory)
    }

    @Provides
    @Singleton
    fun providesBindWorkerToFactory(saveTimeWorkerFactory: SaveTimeWorkerFactory): BindWorkerToFactory {
        return BindWorkerToFactory(saveTimeWorkerFactory)
    }
}