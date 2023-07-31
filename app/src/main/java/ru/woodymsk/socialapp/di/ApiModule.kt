package ru.woodymsk.socialapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.woodymsk.socialapp.BuildConfig
import ru.woodymsk.socialapp.data.api.AuthService
import ru.woodymsk.socialapp.data.api.EventService
import ru.woodymsk.socialapp.data.api.PostService
import ru.woodymsk.socialapp.data.api.ProfileService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideOkhttp(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okhttp: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.BASE_URL)
        .client(okhttp)
        .build()

    @Singleton
    @Provides
    fun providePostService(retrofit: Retrofit): PostService = retrofit.create()

    @Singleton
    @Provides
    fun provideEventService(retrofit: Retrofit): EventService = retrofit.create()

    @Singleton
    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService = retrofit.create()

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create()

}