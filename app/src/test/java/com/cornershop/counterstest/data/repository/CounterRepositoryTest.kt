package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.coVerifyNever
import com.cornershop.counterstest.data.coVerifyOnce
import com.cornershop.counterstest.data.getMockedCounters
import com.cornershop.counterstest.data.helper.ResultWrapper
import com.cornershop.counterstest.data.local.datasource.CountersLocalDataSource
import com.cornershop.counterstest.data.remote.datasource.CounterRemoteDataSource
import com.cornershop.counterstest.domain.entities.Counter
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class CounterRepositoryTest {
	private val localDataSource: CountersLocalDataSource = mockk()
	private val remoteDataSource: CounterRemoteDataSource = mockk()
	private lateinit var repository: CounterRepository

	@Before
	@BeforeAll
	fun setup() {
		clearAllMocks()
		coEvery { localDataSource.updateDatabase(any()) } just Runs
		repository = CounterRepositoryImpl(remoteDataSource, localDataSource)
	}

	@Test
	fun `SHOULD return local counters WHEN local data source has data`() = runBlockingTest {
		coEvery { localDataSource.getCounters() } answers { getMockedCounters() }

		assertIs<ResultWrapper.Success<List<Counter>>>(repository.getCounters())
		coVerifyOnce { localDataSource.getCounters() }
		coVerifyNever { remoteDataSource.getCounters() }
	}

	@Test
	fun `SHOULD return remote counters WHEN local data source has no data`() = runBlockingTest {
		coEvery { localDataSource.getCounters() } answers { emptyList() }

		coEvery { remoteDataSource.getCounters() } answers {
			ResultWrapper.Success(
				getMockedCounters()
			)
		}

		assertIs<ResultWrapper.Success<List<Counter>>>(repository.getCounters())
		coVerifyOnce { localDataSource.getCounters() }
		coVerifyOnce { remoteDataSource.getCounters() }
	}

	@Test
	fun `SHOULD add to remote data source and update local data WHEN addCounter method is successfully called`() =
		runBlockingTest {
			coEvery { remoteDataSource.addCounter(any()) } answers {
				ResultWrapper.Success(
					getMockedCounters()
				)
			}

			val anyText = "Any String"
			repository.addCounter(anyText)

			coVerifyAll {
				remoteDataSource.addCounter(anyText)
				localDataSource.updateDatabase(any())
			}
		}

	@Test
	fun `SHOULD try add to remote data source and NOT update local data WHEN error`() =
		runBlockingTest {
			coEvery { remoteDataSource.addCounter(any()) } answers {
				ResultWrapper.NetworkErrorEntity.Unknown
			}

			val anyText = "Any String"
			repository.addCounter(anyText)

			coVerifyOnce { remoteDataSource.addCounter(anyText) }
			coVerifyNever { localDataSource.updateDatabase(any()) }
		}
}
