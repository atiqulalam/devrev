package com.example.devrev.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devrevsdk.NetworkCallback
import com.devrevsdk.NetworkClient
import com.devrevsdk.NetworkResponse
import com.example.devrev.models.ApiResponse
import com.example.devrev.models.Movie
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkClient: NetworkClient

    private lateinit var mainViewModel: MainViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mainViewModel = MainViewModel(networkClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchPopular_success() = runBlockingTest {
        // Arrange
        val mockMovie = Movie(id = 1, title = "Test Movie")
        val mockResponse = NetworkResponse(200, emptyMap(), body = "{\"results\":[{\"id\":1,\"title\":\"Test Movie\"}]}")
        val apiResponse = ApiResponse(1,results = arrayListOf(mockMovie))

        doAnswer { invocation ->
            val callback = invocation.getArgument<NetworkCallback>(2)
            callback.onSuccess(mockResponse)
            null
        }.`when`(networkClient).get(anyString(), anyMap(), any())

        // Act
        val job = launch {
            mainViewModel.fetchPopular()
        }

        // Assert
        assertTrue(mainViewModel.popularDataLoading.first())
        job.join()
        assertFalse(mainViewModel.popularDataLoading.first())
        assertEquals(arrayListOf(mockMovie), mainViewModel.popularDataFlow.first())
        assertEquals(1, mainViewModel.popularPage)
    }

    @Test
    fun fetchPopular_failure() = runBlockingTest {
        // Arrange
        val mockError = Throwable("Network error")

        doAnswer { invocation ->
            val callback = invocation.getArgument<NetworkCallback>(2)
            callback.onFailure(mockError)
            null
        }.`when`(networkClient).get(anyString(), anyMap(), any())

        // Act
        val job = launch {
            mainViewModel.fetchPopular()
        }

        // Assert
        assertTrue(mainViewModel.popularDataLoading.first())
        job.join()
        assertFalse(mainViewModel.popularDataLoading.first())
        assertTrue(mainViewModel.popularDataFlow.first().isEmpty())
        assertEquals(1, mainViewModel.popularPage)
    }

    @Test
    fun fetchTopRated_success() = runBlockingTest {
        // Arrange
        val mockMovie = Movie(id = 2, title = "Test Movie 2")
        val mockResponse = NetworkResponse(200, emptyMap(),body = "{\"results\":[{\"id\":2,\"title\":\"Test Movie 2\"}]}")
        val apiResponse = ApiResponse(1,results = arrayListOf(mockMovie))

        doAnswer { invocation ->
            val callback = invocation.getArgument<NetworkCallback>(2)
            callback.onSuccess(mockResponse)
            null
        }.`when`(networkClient).get(anyString(), anyMap(), any())

        // Act
        val job = launch {
            mainViewModel.fetchTopRated()
        }

        // Assert
        assertTrue(mainViewModel.topRatedDataLoading.first())
        job.join()
        assertFalse(mainViewModel.topRatedDataLoading.first())
        assertEquals(arrayListOf(mockMovie), mainViewModel.topRatedDataFlow.first())
        assertEquals(2, mainViewModel.latestPage)
    }

    @Test
    fun fetchTopRated_failure() = runBlockingTest {
        // Arrange
        val mockError = Throwable("Network error")

        doAnswer { invocation ->
            val callback = invocation.getArgument<NetworkCallback>(2)
            callback.onFailure(mockError)
            null
        }.`when`(networkClient).get(anyString(), anyMap(), any())

        // Act
        val job = launch {
            mainViewModel.fetchTopRated()
        }

        // Assert
        assertTrue(mainViewModel.topRatedDataLoading.first())
        job.join()
        assertFalse(mainViewModel.topRatedDataLoading.first())
        assertTrue(mainViewModel.topRatedDataFlow.first().isEmpty())
        assertEquals(1, mainViewModel.latestPage)
    }
}
