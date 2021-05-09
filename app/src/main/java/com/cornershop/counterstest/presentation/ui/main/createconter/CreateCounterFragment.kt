package com.cornershop.counterstest.presentation.ui.main.createconter

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.CreateCounterFragmentBinding
import com.cornershop.counterstest.presentation.commons.ViewState
import com.cornershop.counterstest.presentation.commons.errorevent.DialogErrorEvent
import com.cornershop.counterstest.presentation.commons.errorevent.ErrorEvent
import com.cornershop.counterstest.presentation.commons.errorevent.showErrorDialog
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.show
import com.cornershop.counterstest.presentation.commons.util.showGenericErrorDialog
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateCounterFragment : Fragment(R.layout.create_counter_fragment) {
    private val viewModel: CreateCounterViewModel by viewModel()
    private val viewBinding: CreateCounterFragmentBinding by viewBinding()
    private val args: CreateCounterFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupCounterNameEditText()
        setupSeeExamplesTextView()
        setupSaveClickListener()
        setupLiveData()
        setupCloseClickListener()
    }

    private fun setupCloseClickListener() = with(viewBinding) {
        toolbar.closeButton.setOnClickListener {
            navigateToCountersFragment()
        }
    }

    private fun setupLiveData() {
        viewModel.counterLiveData.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ViewState.Loading -> {
                    showLoading()
                }
                is ViewState.Success,
                is ViewState.SuccessEmpty -> {
                    hideLoading()
                    navigateToCountersFragment()
                }
                is ViewState.Error -> {
                    hideLoading()
                    dealWithError(viewState.errorEvent)
                }
            }
        }
    }

    private fun navigateToCountersFragment() {
        findNavController()
            .navigate(CreateCounterFragmentDirections.actionCreateCounterFragmentToCountersFragment())
    }

    private fun dealWithError(errorEvent: ErrorEvent) {
        when (errorEvent) {
            is DialogErrorEvent -> {
                context?.run {
                    errorEvent.showErrorDialog(this)
                }
            }
            else -> context?.showGenericErrorDialog()
        }
    }

    private fun setupSaveClickListener() = with(viewBinding) {
        toolbar.saveButton.setOnClickListener {
            onSave()
        }
    }

    private fun onSave() = with(viewBinding) {
        if (counterNameEditText.text.isNullOrBlank()) {
            counterNameInputLayout.error = getString(R.string.save_counter_empty_name_error)
            return@with
        }
        viewModel.createCounter(counterNameEditText.text.toString())
    }

    private fun setupSeeExamplesTextView() {
        setClickableText(
            viewBinding.seeExamplesTextView,
            getString(R.string.create_counter_disclaimer),
            getString(R.string.create_counter_disclaimer_examples)
        )
    }

    private fun setupCounterNameEditText() = with(viewBinding) {
        counterNameEditText.setOnFocusChangeListener { editText: View, hasFocus: Boolean ->
            editText as TextInputEditText
            editText.hint = if (hasFocus) {
                resources.getStringArray(R.array.drinks_array).random()
            } else {
                EMPTY_STRING
            }
        }

        counterNameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSave()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        counterNameEditText.addTextChangedListener {
            counterNameInputLayout.error = null
        }

        args.example?.run {
            counterNameEditText.setText(this)
        }
    }

    private fun showLoading() = with(viewBinding.toolbar) {
        loadingProgressBar.show()
        saveButton.hide()
    }

    private fun hideLoading() = with(viewBinding.toolbar) {
        loadingProgressBar.hide()
        saveButton.show()
    }

    private fun setClickableText(view: TextView, firstSpan: String, secondSpan: String) {
        val builder = SpannableStringBuilder()
        val unClickableSpan = SpannableString("$firstSpan ")
        val span = SpannableString(secondSpan)

        builder.append(unClickableSpan)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                findNavController()
                    .navigate(
                        CreateCounterFragmentDirections.actionCreateCounterFragmentToCounterExamplesFragment()
                    )
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            }
        }
        builder.append(span)
        builder.setSpan(
            clickableSpan,
            firstSpan.length,
            firstSpan.length + secondSpan.length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        view.setText(builder, TextView.BufferType.SPANNABLE)
        view.movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        private const val EMPTY_STRING = ""
    }
}