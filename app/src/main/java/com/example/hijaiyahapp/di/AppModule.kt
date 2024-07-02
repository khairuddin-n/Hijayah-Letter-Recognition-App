package com.example.hijaiyahapp.di

import android.content.Context
import com.example.hijaiyahapp.repository.HijaiyahRepository
import com.example.hijaiyahapp.util.HijaiyahRecognizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideHijaiyahRecognizer(@ApplicationContext context: Context): HijaiyahRecognizer {
        return HijaiyahRecognizer(context)
    }

    @Provides
    @Singleton
    fun provideRepository(hijaiyahRecognizer: HijaiyahRecognizer): HijaiyahRepository {
        return HijaiyahRepository(hijaiyahRecognizer)
    }
}