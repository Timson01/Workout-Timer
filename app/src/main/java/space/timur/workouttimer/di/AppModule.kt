package space.timur.workouttimer.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import space.timur.workouttimer.data.repository.TimerRepositoryImpl
import space.timur.workouttimer.domain.repository.TimerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun timerRepository(): TimerRepository {
        return TimerRepositoryImpl()
    }

}