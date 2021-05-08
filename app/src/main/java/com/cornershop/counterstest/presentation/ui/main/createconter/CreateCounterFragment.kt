package com.cornershop.counterstest.presentation.ui.main.createconter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CreateCounterFragmentBinding
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.show
import com.cornershop.counterstest.presentation.ui.main.counters.CountersViewModel
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCounterFragment : Fragment(R.layout.create_counter_fragment) {
    private val viewModel: CountersViewModel by viewModel()
    private val viewBinding: CreateCounterFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.counterNameEditText.setOnFocusChangeListener { editText: View, hasFocus: Boolean ->
            editText as TextInputEditText
            editText.hint = if (hasFocus) {
                resources.getStringArray(R.array.drinks_array).random()
            } else {
                EMPTY_STRING
            }
        }
    }

    fun showLoading() = with(viewBinding.toolbar) {
        loadingProgressBar.show()
        saveButton.hide()
    }

    fun hideLoading() = with(viewBinding.toolbar) {
        loadingProgressBar.show()
        saveButton.hide()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}