package ir.nwise.app.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.NetworkManager
import ir.nwise.app.domain.entities.PhotoModel
import ir.nwise.app.domain.models.PhotoResponse
import ir.nwise.app.domain.usecase.GetPhotoResultUseCase
import ir.nwise.app.utils.CoroutineTestRule
import ir.nwise.app.utils.captureEmittedData
import ir.nwise.app.utils.captureLastEmittedValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    private lateinit var testViewModel: HomeViewModel

    private lateinit var useCase: GetPhotoResultUseCase

    @Mock
    private lateinit var appRepository: AppRepository

    @Mock
    private lateinit var networkManager: NetworkManager

    val list = listOf(
        PhotoResponse(
            id = 1,
            userName = "userName",
            largeImageUrl = "largeImageURL",
            webFormatUrl = "webformatURL",
            previewImageUrl = "previewImageUrl",
            userImageUrl = "userImageUrl",
            commentNumber = "commentNumber",
            likeNumber = "likeNumber",
            tags = "tags",
            downloadNumber = "downloadNumber"
        )
    )

    @Before
    fun setUp() {
        useCase = GetPhotoResultUseCase(
            appRepository = appRepository,
            dispatchers = coroutinesTestRule.testDispatcherProvider
        )
        testViewModel = HomeViewModel(useCase, networkManager)
    }

    @Test
    fun `#getPhotos() must emit #Loading and #Loaded ViewStates`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {

            //given
            val observer = testViewModel.liveData.captureEmittedData()
            whenever(networkManager.hasNetwork()).thenAnswer {
                true
            }
            whenever(appRepository.getPhotoResult(PhotoModel())).thenAnswer {
                list
            }

            //when
            testViewModel.getPhotos("")

            //Then
            Assert.assertNotNull(testViewModel.liveData.value)
            Assert.assertEquals(
                listOf(HomeViewState.Loading, HomeViewState.Loaded(list)),
                observer.invoke()
            )
        }

    @Test
    fun `#getPhotos() must return photo list`() =
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //given
            val observer = testViewModel.liveData.captureLastEmittedValue()
            whenever(networkManager.hasNetwork()).thenAnswer {
                true
            }
            whenever(appRepository.getPhotoResult(PhotoModel())).thenAnswer {
                list
            }

            //when
            testViewModel.getPhotos("")

            //Then
            Assert.assertNotNull(testViewModel.liveData.value)
            Assert.assertEquals(
                testViewModel.liveData.value,
                observer.invoke()
            )
        }


    @Test
    fun `when fetching results fails then return an error and must emit #Loading and #Error ViewStates`() {
        val exception = Mockito.mock(HttpException::class.java)
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //given
            val observer = testViewModel.liveData.captureLastEmittedValue()
            whenever(appRepository.getPhotoResult(PhotoModel())).thenAnswer {
                throw (exception)
            }
            whenever(networkManager.hasNetwork()).thenAnswer {
                true
            }

            //when
            testViewModel.getPhotos("")

            //Then
            Assert.assertNotNull(testViewModel.liveData.value)
            Assert.assertEquals(
                HomeViewState.Error(exception),
                observer.invoke()
            )
        }
    }

    @Test
    fun `when there is no internet connection, it must emit #NoInternetConnectionException`() {
        coroutinesTestRule.testDispatcher.runBlockingTest {
            //given
            val observer = testViewModel.liveData.captureLastEmittedValue()
            whenever(networkManager.hasNetwork()).thenAnswer {
                false
            }

            //when
            testViewModel.getPhotos("")

            //Then
            Assert.assertNotNull(testViewModel.liveData.value)
            Assert.assertEquals(
                testViewModel.liveData.value,
                observer.invoke()
            )
        }
    }


}

