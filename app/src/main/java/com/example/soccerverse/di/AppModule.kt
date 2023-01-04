package com.example.soccerverse.di

import com.example.soccerverse.data.remote.SoccerverseApi
import com.example.soccerverse.repository.SoccerVerseRepository
import com.example.soccerverse.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLeagueRepo(
        api: SoccerverseApi
    ) = SoccerVerseRepository(api)

    @Singleton
    @Provides
    fun provideSoccerVerseAPi(): SoccerverseApi {
        val client = OkHttpClient.Builder().apply {
            addInterceptor(RetrofitInterceptor())
        }.build()

        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .build()
            .create(SoccerverseApi::class.java)
    }
}