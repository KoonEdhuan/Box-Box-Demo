package com.example.boxboxdemo.di

import com.example.boxboxdemo.data.repository.MyRepository
import com.example.boxboxdemo.data.repositoryImpl.MyRepositoryImpl
import com.example.boxboxdemo.data.service.MyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyRepository(
        myApiService: MyApiService
    ): MyRepository {
        return MyRepositoryImpl(myApiService)
    }

    @Provides
    @Singleton
    fun provideMyApiService(): MyApiService {
        return Retrofit.Builder()
            .baseUrl("https://mocki.io/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApiService::class.java)
    }
}
