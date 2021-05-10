package com.cornershop.counterstest.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CounterUiModel(
	val id: String,
	val title: String,
	val count: Int
) : Parcelable