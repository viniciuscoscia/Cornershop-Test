package com.cornershop.counterstest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.AfterClass
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseTest {
	@Rule
	@JvmField
	val instantTaskExecutor = InstantTaskExecutorRule()

	@Before
	@BeforeAll
	internal fun beforeTestExecute() {
		beforeTest()
	}

	@BeforeEach
	private fun beforeEachTestExecute() {
		clearAllMocks()
		beforeEachTest()
	}

	open fun beforeEachTest() {}

	abstract fun beforeTest()

	companion object {
		@JvmStatic
		@AfterClass
		fun tearDown() {
			unmockkAll()
		}
	}
}