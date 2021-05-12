package com.cornershop.counterstest.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.cornershop.counterstest.data.local.entity.CounterDatabaseEntity
import com.cornershop.counterstest.domain.entities.Counter
import io.mockk.MockKVerificationScope
import io.mockk.coVerify
import io.mockk.verify
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun getMockedCounters() = listOf(
	Counter("1", "mock1", 1),
	Counter("2", "mock2", 2),
	Counter("3", "mock3", 3),
	Counter("4", "mock4", 4),
	Counter("5", "mock5", 5),
	Counter("6", "mock6", 6)
)

fun getMockedDatabaseEntityCounters() = getMockedCounters().map {
	CounterDatabaseEntity(
		it.id, it.title, it.count
	)
}

fun verifyOnce(verificationBlock: MockKVerificationScope.() -> Unit) =
	verify(exactly = 1, verifyBlock = verificationBlock)

fun coVerifyOnce(verificationBlock: suspend MockKVerificationScope.() -> Unit) =
	coVerify(exactly = 1, verifyBlock = verificationBlock)

fun verifyNever(verificationBlock: MockKVerificationScope.() -> Unit) =
	verify(exactly = 0, verifyBlock = verificationBlock)

fun coVerifyNever(verificationBlock: suspend MockKVerificationScope.() -> Unit) =
	coVerify(exactly = 0, verifyBlock = verificationBlock)

/* Copyright 2019 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
fun <T> LiveData<T>.getOrAwaitValue(
	time: Long = 2,
	timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
	var data: T? = null
	val latch = CountDownLatch(1)
	val observer = object : Observer<T> {
		override fun onChanged(o: T?) {
			data = o
			latch.countDown()
			this@getOrAwaitValue.removeObserver(this)
		}
	}

	this.observeForever(observer)

	// Don't wait indefinitely if the LiveData is not set.
	if (!latch.await(time, timeUnit)) {
		throw TimeoutException("LiveData value was never set.")
	}

	@Suppress("UNCHECKED_CAST")
	return data as T
}