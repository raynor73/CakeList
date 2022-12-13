package ilapin.cakelist.ui

import ilapin.cakelist.domain.Cake
import ilapin.cakelist.domain.CakesRepository
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CakesListViewModelTest {

    private val cakesRepository: CakesRepository = mock()

    private val sut = CakesListViewModel(cakesRepository)

    @Test
    fun onCreate_ifThereIsNothingBeingLoaded_requestCakesFromRepository() {
        // Given
        given(cakesRepository.getCakesList()).willReturn(Single.never())

        // Then
        assertFalse(sut.state.isLoading)

        // And when
        sut.onCreate(mock())

        // Then
        assertTrue(sut.state.isLoading)
        verify(cakesRepository).getCakesList()
    }

    @Test
    fun onCreate_cakesLoadedSuccessfully_cakesArePresentedWithDuplicatesRemovedAndSortingApplied() {
        // Given
        val rawCakesList = listOf(
            Cake(
                title = "Cake A",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            ),
            Cake(
                title = "Cake A",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            ),
            Cake(
                title = "Cake C",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            ),
            Cake(
                title = "Cake B",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            )
        )

        val filteredAndSortedCakesList = listOf(
            Cake(
                title = "Cake A",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            ),
            Cake(
                title = "Cake B",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            ),
            Cake(
                title = "Cake C",
                description = "Cake A description",
                imageUrl = "https://example.com/cake.jpg"
            )
        )

        given(cakesRepository.getCakesList()).willReturn(Single.just(rawCakesList))

        // When
        sut.onCreate(mock())

        // Then
        assertEquals(filteredAndSortedCakesList, sut.state.cakes)
    }

    @Test
    fun onCreate_errorOccurredDuringCakesLoading_errorIsPresented() {
        // Given
        given(cakesRepository.getCakesList()).willReturn(Single.error(Exception()))

        // When
        sut.onCreate(mock())

        // Then
        assertTrue(sut.state.isError)
    }
}