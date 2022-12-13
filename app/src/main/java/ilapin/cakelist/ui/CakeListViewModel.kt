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

        loadIfIdle()
    }

    fun onCakeClicked(cake: Cake) {
        state = state.copy(popUpDescription = cake.description)
    }

    fun onPopUpDescriptionDismissRequested() {
        state = state.copy(popUpDescription = null)
    }

    fun onRefresh() {
        if (state.isRefreshing) {
            return
        }

        state = state.copy(isRefreshing = true)

        loadCakes()
    }

    fun onRetryClicked() {
        loadIfIdle()
    }

    private fun loadIfIdle() {
        if (state.isLoading) {
            return
        }

        state = state.copy(isLoading = true)

        loadCakes()
    }

    private fun loadCakes() {
        cakesRepository
            .getCakesList()
            .map { cakes -> cakes.distinct().sortedBy { it.title } }
            .subscribe { cakes, error: Throwable? ->
                state = if (error == null) {
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isError = false,
                        cakes = cakes
                    )
                } else {
                    state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isError = true,
                    )
                }
            }
    }

    data class State(
        val popUpDescription: String? = null,
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val isRefreshing: Boolean = false,
        val cakes: List<Cake> = emptyList()
    )
}