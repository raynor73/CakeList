package ilapin.cakelist.dataSources

import ilapin.cakelist.common.SchedulersProvider
import ilapin.cakelist.domain.Cake
import ilapin.cakelist.repositories.CakesDataSource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ApiCakesDataSource @Inject constructor(
    private val schedulersProvider: SchedulersProvider,
    private val api: CakesApi,
): CakesDataSource {

    override fun getCakesList(): Single<List<Cake>> {
        return api
            .getCakesList()
            .subscribeOn(schedulersProvider.io())
            .map { dtos: List<CakeDto?> ->
                dtos.mapNotNull { it?.toCake() }
            }
            .observeOn(schedulersProvider.ui())
    }
}