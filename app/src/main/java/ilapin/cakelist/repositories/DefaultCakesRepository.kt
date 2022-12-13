package ilapin.cakelist.repositories

import ilapin.cakelist.domain.Cake
import ilapin.cakelist.domain.CakesRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class DefaultCakesRepository @Inject constructor(
    private val cakesDataSource: CakesDataSource
) : CakesRepository {

    override fun getCakesList(): Single<List<Cake>> {
        return cakesDataSource.getCakesList()
    }
}