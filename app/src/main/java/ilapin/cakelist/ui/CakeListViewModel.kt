package ilapin.cakelist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import ilapin.cakelist.domain.Cake
import ilapin.cakelist.domain.CakesRepository
import javax.inject.Inject

class CakeListViewModel @Inject constructor(
    private val cakesRepository: CakesRepository
) : ViewModel(), DefaultLifecycleObserver {

    var state: State by mutableStateOf(State())
        private set

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        loadCakes()
    }

    fun loadCakes() {
        if (state.isLoading) {
            return
        }

        state = state.copy(isLoading = true)

        cakesRepository
            .getCakesList()
            .subscribe { cakes, error: Throwable? ->
                state = if (error == null) {
                    state.copy(
                        isLoading = false,
                        isError = false,
                        cakes = cakes
                    )
                } else {
                    state.copy(isError = true)
                }
            }
    }

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val cakes: List<Cake> = emptyList()
    )
}