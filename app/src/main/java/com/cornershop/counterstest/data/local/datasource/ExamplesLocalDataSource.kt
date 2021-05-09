package com.cornershop.counterstest.data.local.datasource

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.entities.ExampleEntity

interface ExamplesLocalDataSource {
    fun getExamples(): List<ExampleEntity>
}

class ExamplesLocalDataSourceImpl(
    private val context: Context
) : ExamplesLocalDataSource {
    override fun getExamples(): List<ExampleEntity> {
        return listOf(
            getExampleEntity(context, R.string.drinks, R.array.drinks_array),
            getExampleEntity(context, R.string.foods, R.array.food_array),
            getExampleEntity(context, R.string.misc, R.array.misc_array)
        )
    }

    private fun getExampleEntity(
        context: Context,
        @StringRes entityTitle: Int,
        @ArrayRes arrayRes: Int
    ) = with(context) {
        ExampleEntity(
            title = getString(entityTitle),
            examples = resources.getStringArray(arrayRes).toList()
        )
    }
}