package ilapin.cakelist.domain

import io.reactivex.rxjava3.core.Single

interface CakesRepository {

    fun getCakesList(): Single<List<Cake>>
}