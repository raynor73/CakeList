package ilapin.cakelist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ilapin.cakelist.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://gist.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @Provides
        fun providesLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
        }

        @Provides
        fun providesOkHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder().apply {
                addInterceptor(loggingInterceptor)
            }

            return okHttpClientBuilder.build()
        }
    }
}