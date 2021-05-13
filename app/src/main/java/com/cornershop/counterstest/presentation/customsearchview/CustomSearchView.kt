package com.cornershop.counterstest.presentation.customsearchview

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.EditText
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.commons.util.hide
import com.cornershop.counterstest.presentation.commons.util.show

class CustomSearchView : ConstraintLayout {
	init {
		inflate(context, R.layout.custom_search_bar, this)
	}

	constructor(context: Context) : super(context)

	constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

	constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : super(
		context,
		attrs,
		defStyleAttr
	)

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	constructor(
		context: Context,
		attrs: AttributeSet,
		defStyleAttr: Int = 0,
		defStyleRes: Int = 0
	) : super(context, attrs, defStyleAttr, defStyleRes)

	private val searchTextView: EditText = findViewById(R.id.search_text)
	private val clearTextButton: ImageButton = findViewById(R.id.clear_text_button)
	private val backButton: ImageButton = findViewById(R.id.back_button)
	private val enabledSearchLayout: ConstraintLayout = findViewById(R.id.enabled_search_layout)
	private val disabledSearchLayout: CardView = findViewById(R.id.disabled_search_layout)
	private var onExitSearchModeListener: (() -> Unit)? = null
	private var onTextChangedListener: ((CustomSearchViewState) -> Unit)? = null

	fun initView(
		onSearchViewClick: () -> Unit,
		onTextChangedListener: (CustomSearchViewState) -> Unit,
		onExitSearchModeListener: () -> Unit
	) {
		this.onExitSearchModeListener = onExitSearchModeListener
		this.onTextChangedListener = onTextChangedListener

		searchTextView.addTextChangedListener {
			val searchText = searchTextView.text.toString()

			onTextChangedListener(
				if (searchText.isNotBlank()) {
					CustomSearchViewState.Writing(searchText)
				} else {
					CustomSearchViewState.EmptyText
				}
			)
		}

		backButton.setOnClickListener {
			exitSearchMode()
		}

		disabledSearchLayout.setOnClickListener {
			enableSearch()
			searchTextView.requestFocus()
			onSearchViewClick()
		}

		clearTextButton.setOnClickListener {
			searchTextView.setText("")
		}
	}

	private fun enableSearch() {
		enabledSearchLayout.show()
		disabledSearchLayout.hide()
	}

	fun exitSearchMode() {
		enabledSearchLayout.hide()
		disabledSearchLayout.show()
		onExitSearchModeListener?.invoke()
	}

	fun getSearchText() = searchTextView.text.toString()

	fun requestNewSearch() {
		onTextChangedListener?.invoke(CustomSearchViewState.Writing(searchTextView.text.toString()))
	}
}