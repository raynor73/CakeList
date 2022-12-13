package ilapin.cakelist.repositories

import ilapin.cakelist.domain.Cake
import io.reactivex.rxjava3.core.Single

interface CakesDataSource {

    fun getCakesList(): Single<List<Cake>>
}