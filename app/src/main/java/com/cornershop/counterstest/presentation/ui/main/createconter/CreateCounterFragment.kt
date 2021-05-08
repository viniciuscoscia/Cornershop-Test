package com.cornershop.counterstest.presentation.ui.main.createconter

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CreateCounterFragmentBinding
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.CountersErrorEvents
import com.cornershop.counterstest.presentation.commons.errorevent.ErrorEvent
import com.cornershop.counterstest.presentation.commons.errorevent.showErrorDialog
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.show
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCounterFragment : Fragment(R.layout.create_counter_fragment) {
    private val viewModel: CreateCounterViewModel by viewModel()
    private val viewBinding: CreateCounterFragmentBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupView() {
        setupCounterNameEditText()
        setupSeeExamplesTextView()
        setupSaveClickListener()
        setupLiveData()
    }

    private fun setupLiveData() {
        viewModel.counterLiveData.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success -> {
                    hideLoading()
                }
                is ViewState.Error -> {
                    dealWithError(viewState.errorEvent)
                    hideLoading()
                }
            }
        }
    }

    private fun dealWithError(errorEvent: ErrorEvent) {
        when (errorEvent) {
            is CountersErrorEvents.CreateCounterErrorEvent -> {
                context?.run {
                    errorEvent.showErrorDialog(this)
                }
            }
            else -> {

            }
        }
    }

    private fun setupSaveClickListener() = with(viewBinding) {
        toolbar.saveButton.setOnClickListener {
            if (counterNameEditText.text.isNullOrBlank()) {
                counterNameInputLayout.error = getString(R.string.save_counter_empty_name_error)
                return@setOnClickListener
            }
            viewModel.createCounter(counterNameEditText.text.toString())
        }
    }

    private fun setupSeeExamplesTextView() {
        setClickableText(
            viewBinding.seeExamplesTextView,
            getString(R.string.create_counter_disclaimer),
            getString(R.string.create_counter_disclaimer_examples)
        )
    }

    private fun setupCounterNameEditText() {
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

    private fun setClickableText(view: TextView, firstSpan: String, secondSpan: String) {
        val builder = SpannableStringBuilder()
        val unClickableSpan = SpannableString(firstSpan)
        val span = SpannableString(" $secondSpan")

        builder.append(unClickableSpan);
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                // TODO start new fragment
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
            }
        }
        builder.append(span);
        builder.setSpan(
            clickableSpan,
            firstSpan.length,
            firstSpan.length + secondSpan.length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        view.setText(builder, TextView.BufferType.SPANNABLE)
        view.movementMethod = LinkMovementMethod.getInstance();
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}