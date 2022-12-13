package ilapin.cakelist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ilapin.cakelist.common.SchedulersProvider
import ilapin.cakelist.dataSources.ApiCakesDataSource
import ilapin.cakelist.domain.CakesRepository
import ilapin.cakelist.platform.AndroidSchedulersProvider
import ilapin.cakelist.repositories.CakesDataSource
import ilapin.cakelist.repositories.DefaultCakesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindCakesDataSource(apiCakesDataSource: ApiCakesDataSource): CakesDataSource

    @Binds
    abstract fun bindCakesRepository(defaultCakesRepository: DefaultCakesRepository): CakesRepository

    @Binds
    abstract fun bindSchedulersProvider(androidSchedulersProvider: AndroidSchedulersProvider): SchedulersProvider
}