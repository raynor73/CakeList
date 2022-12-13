package ilapin.cakelist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import ilapin.cakelist.R
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
                    CakeListScreen(
                        state = viewModel.state,
                        onPopUpDescriptionDismissRequested = { viewModel.onPopUpDescriptionDismissRequested() },
                        onCakeClick = { cake -> viewModel.onCakeClicked(cake) },
                        onRefresh = { viewModel.onRefresh() }
                    )
                }
            }
        })

        lifecycle.addObserver(viewModel)
    }
}

@Composable
private fun CakeListScreen(
    state: CakeListViewModel.State,
    onPopUpDescriptionDismissRequested: () -> Unit,
    onCakeClick: (Cake) -> Unit,
    onRefresh: () -> Unit
) {
    when {
        state.isLoading -> LoadingState()
        state.isError -> {}
        else -> CakesState(state = state, onCakeClick = onCakeClick, onRefresh = onRefresh)
    }

    if (state.popUpDescription != null) {
        AlertDialog(
            onDismissRequest = onPopUpDescriptionDismissRequested,
            title = { Text(stringResource(R.string.popup_title)) },
            text = { Text(state.popUpDescription) },
            confirmButton = {
                Button(onClick = onPopUpDescriptionDismissRequested) {
                    Text(stringResource(R.string.general_ok))
                }
            }
        )
    }
}

@Composable
private fun Cake(cake: Cake, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(cake.imageUrl).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(cake.title)
        }
        Divider()
    }
}

@Composable
private fun CakesState(
    state: CakeListViewModel.State,
    onCakeClick: (Cake) -> Unit,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = state.isRefreshing),
        onRefresh = onRefresh
    ) {
        LazyColumn {
            this.items(state.cakes) { cake -> Cake(cake) { onCakeClick(cake) } }
        }
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
    CakeListScreen(
        state = CakeListViewModel.State(cakes = cakes),
        onPopUpDescriptionDismissRequested = {},
        onCakeClick = {},
        onRefresh = {}
    )
}

@Composable
@Preview(
    apiLevel = 33,
    showSystemUi = true,
    device = Devices.PIXEL_4
)
private fun CakeListScreenPopUpPreview() {
    CakeListScreen(
        state = CakeListViewModel.State(
            popUpDescription = "A cheesecake made of lemon",
            isLoading = true,
        ),
        onPopUpDescriptionDismissRequested = {},
        onCakeClick = {},
        onRefresh = {}
    )
}

@Composable
@Preview(
    apiLevel = 33,
    showSystemUi = true,
    device = Devices.PIXEL_4
)
private fun CakeListScreenLoadingPreview() {
    CakeListScreen(
        state = CakeListViewModel.State(isLoading = true),
        onPopUpDescriptionDismissRequested = {},
        onCakeClick = {},
        onRefresh = {}
    )
}