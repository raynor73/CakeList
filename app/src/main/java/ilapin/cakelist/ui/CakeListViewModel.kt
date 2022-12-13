package ilapin.cakelist.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import ilapin.cakelist.domain.Cake
import ilapin.cakelist.domain.CakesRepository
import javax.inject.Inject

class CakeListViewModel @Inject constructor(
    private val cakesRepository: CakesRepository
) : ViewModel(), DefaultLifecycleObserver {

    var state: State by mutableStateOf(State())
        private set

    data class State(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val cakes: List<Cake> = emptyList()
    )
}