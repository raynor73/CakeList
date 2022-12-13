package ilapin.cakelist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ilapin.cakelist.dataSources.CakesApi
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    companion object {

        @Provides
        fun provideCakesApi(retrofit: Retrofit): CakesApi {
            return retrofit.create(CakesApi::class.java)
        }
    }
}