package ilapin.cakelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import ilapin.cakelist.domain.Cake
import ilapin.cakelist.ui.theme.CakeListTheme
import javax.inject.Inject

@AndroidEntryPoint
class CakesListActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: CakeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ComposeView(this).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CakeListTheme {
                    CakeListScreen(viewModel.state)
                }
            }
        })

        lifecycle.addObserver(viewModel)
    }
}

@Composable
private fun CakeListScreen(state: CakeListViewModel.State) {
    when {
        state.isLoading -> LoadingState()
        state.isError -> {}
        else -> CakesState(state.cakes)
    }
}

@Composable
private fun Cake(cake: Cake) {
    Column {
        Row {
            Text(cake.title)
        }
        Divider()
    }
}

@Composable
private fun CakesState(cakes: List<Cake>) {
    LazyColumn {
        items(cakes) { cake -> Cake(cake) }
    }
}

@Composable
private fun LoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
@Preview(
    apiLevel = 33,
    showSystemUi = true,
    device = Devices.PIXEL_4
)
private fun CakeListScreenCakesPreview() {
    val cakes = listOf(
        Cake(
            title = "Cake 1",
            description = "",
            imageUrl = ""
        ),
        Cake(
            title = "Cake 2",
            description = "",
            imageUrl = ""
        ),
        Cake(
            title = "Cake 3",
            description = "",
            imageUrl = ""
        )
    )
    CakeListScreen(CakeListViewModel.State(cakes = cakes))
}

@Composable
@Preview(
    apiLevel = 33,
    showSystemUi = true,
    device = Devices.PIXEL_4
)
private fun CakeListScreenLoadingPreview() {
    CakeListScreen(CakeListViewModel.State(isLoading = true))
}