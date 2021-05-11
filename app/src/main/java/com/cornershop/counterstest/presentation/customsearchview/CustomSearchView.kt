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

	private val searchText: EditText = findViewById(R.id.search_text)
	private val clearTextButton: ImageButton = findViewById(R.id.clear_text_button)
	private val backButton: ImageButton = findViewById(R.id.back_button)
	private val enabledSearchLayout: ConstraintLayout = findViewById(R.id.enabled_search_layout)
	private val disabledSearchLayout: CardView = findViewById(R.id.disabled_search_layout)
	private val onSearchDisabled: (() -> Unit)? = null

	fun initView(
		onSearchViewClick: () -> Unit,
		onTextChangedListener: (CustomSearchViewState) -> Unit,
		onBackPressedListener: () -> Unit,
		onSearchDisabled: () -> Unit
	) {
		searchText.addTextChangedListener {
			val searchText = searchText.text.toString()

			onTextChangedListener(
				if (searchText.isNotBlank()) {
					CustomSearchViewState.Writing(searchText)
				} else {
					CustomSearchViewState.EmptyText
				}
			)
		}

		backButton.setOnClickListener {
			disableSearch()
			onSearchDisabled()
			onBackPressedListener()
		}

		disabledSearchLayout.setOnClickListener {
			enableSearch()
			searchText.requestFocus()
			onSearchViewClick()
		}

		clearTextButton.setOnClickListener {
			searchText.setText("")
		}
	}

	private fun enableSearch() {
		enabledSearchLayout.show()
		disabledSearchLayout.hide()
	}

	private fun disableSearch() {
		enabledSearchLayout.hide()
		disabledSearchLayout.show()
		onSearchDisabled?.invoke()
	}
}