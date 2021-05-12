package com.cornershop.counterstest.presentation.ui.main.counters

import com.cornershop.counterstest.BaseTest
import com.cornershop.counterstest.data.getMockedCounters
import com.cornershop.counterstest.data.getOrAwaitValue
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.domain.usecases.counter.GetCountersUseCase
import com.cornershop.counterstest.presentation.commons.errorevent.CountersErrorEvents
import com.cornershop.counterstest.presentation.commons.viewstate.ViewState
import com.cornershop.counterstest.presentation.model.CountersFragmentUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test
import org.junit.jupiter.api.fail
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CountersViewModelTest : BaseTest() {
	private lateinit var countersViewModel: CountersViewModel
	private val getCountersUseCase: GetCountersUseCase = mockk()
	private val testCoroutineDispatcher = TestCoroutineDispatcher()

	override fun beforeTest() {
		countersViewModel = CountersViewModel(
			getCountersUseCase = getCountersUseCase,
			increaseCounterUseCase = mockk(),
			searchCountersByTextUseCase = mockk(),
			deleteCounterUseCase = mockk(),
			decreaseCounterUseCase = mockk(),
			coroutineDispatcher = testCoroutineDispatcher
		)
	}

	@After
	fun tearDown() {
		testCoroutineDispatcher.cleanupTestCoroutines()
	}

	@Test
	fun `SHOULD emit ViewState Success WHEN success executing getCountersUseCase`() =
		testCoroutineDispatcher.runBlockingTest {
			coEvery { getCountersUseCase() } answers { ResultWrapper.Success(getMockedCounters()) }

			testCoroutineDispatcher.pauseDispatcher()
			countersViewModel.getCounters()

			val loading = countersViewModel.countersLiveData.getOrAwaitValue()

			testCoroutineDispatcher.resumeDispatcher()

			val result = countersViewModel.countersLiveData.getOrAwaitValue()

			assertTrue { loading is ViewState.Loading }
			assertTrue { result is ViewState.Success<CountersFragmentUiModel> }
		}

	@Test
	fun `SHOULD emit ViewState GetCountersNetworkUnavailable WHEN timeout or NoInternetConnection executing getCountersUseCase`() =
		testCoroutineDispatcher.runBlockingTest {
			coEvery { getCountersUseCase() } answers { ResultWrapper.NetworkErrorEntity.Timeout }

			testCoroutineDispatcher.pauseDispatcher()
			countersViewModel.getCounters()

			val loading = countersViewModel.countersLiveData.getOrAwaitValue()

			testCoroutineDispatcher.resumeDispatcher()

			val result = countersViewModel.countersLiveData.getOrAwaitValue()

			assertTrue { loading is ViewState.Loading }

			when (result) {
				is ViewState.Error -> {
					assertTrue { result.errorEvent is CountersErrorEvents.GetCountersNetworkUnavailable }
				}
				else -> {
					fail { "Result should be failure" }
				}
			}
		}
}