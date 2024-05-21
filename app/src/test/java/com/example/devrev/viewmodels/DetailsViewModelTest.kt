package com.example.devrev.viewmodels


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrevsdk.NetworkCallback
import com.devrevsdk.NetworkClient
import com.devrevsdk.NetworkResponse
import com.example.devrev.models.MovieDetail
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertFalse
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkClient: NetworkClient

    private lateinit var detailsViewModel: DetailsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        detailsViewModel = DetailsViewModel(networkClient)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getMovieDetails_success() = runTest {
        // Arrange
        val movieId = "123"
        val mockMovieDetail = MovieDetail(id = 123, title = "Test Movie")
        val mockResponse = NetworkResponse(200, emptyMap(), body = "{\"id\":123,\"title\":\"Test Movie\"}")

        doAnswer { invocation ->
            val callback = invocation.getArgument<NetworkCallback>(2)
            callback.onSuccess(mockResponse)
            null
        }.`when`(networkClient).get(
            ArgumentMatchers.anyString(),
            ArgumentMatchers.any<Map<String, String>>(),
            ArgumentMatchers.any<Map<String, String>>(),
            ArgumentMatchers.any<NetworkCallback>()
        )

        // Act
        val job = launch {
            detailsViewModel.getMovieDetails(movieId)
        }

        // Assert
        assertTrue(detailsViewModel.detailsDataLoading.first())
        job.join()
        assertFalse(detailsViewModel.detailsDataLoading.first())
        assertEquals(mockMovieDetail, detailsViewModel.detailsDataFlow.first())
    }


    @Test
    fun getMovieDetails_failure() = runBlockingTest {
        // Arrange
        val movieId = "123"
        val mockError = Throwable("Network error")

        doAnswer { invocation ->
            val callback = invocation.getArgument<NetworkCallback>(2)
            callback.onFailure(mockError)
            null
        }.`when`(networkClient).get(anyString(), anyMap(), anyMap(), any())

        // Act
        val job = launch {
            detailsViewModel.getMovieDetails(movieId)
        }

        // Assert
        assert(detailsViewModel.detailsDataLoading.first())
        job.join()
        assert(!detailsViewModel.detailsDataLoading.first())
        assert(detailsViewModel.detailsDataFlow.first() == null)
    }
}
