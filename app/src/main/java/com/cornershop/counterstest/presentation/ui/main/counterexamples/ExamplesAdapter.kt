package com.cornershop.counterstest.presentation.ui.main.counterexamples

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.databinding.ExamplesLayoutBinding
import com.cornershop.counterstest.presentation.model.ExampleUiModel
import com.google.android.material.chip.Chip

class ExamplesAdapter(
    private val examples: List<ExampleUiModel>,
    private val onExampleClickListener: (example: String) -> Unit
) : RecyclerView.Adapter<ExamplesAdapter.ExamplesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamplesViewHolder {
        val view = ExamplesLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExamplesViewHolder(view)
    }

    override fun onBindViewHolder(examplesViewHolder: ExamplesViewHolder, position: Int) {
        val chipEntity = examples[position]

        examplesViewHolder.viewBinding.chipGroupTitle.text = chipEntity.title

        chipEntity.examples.forEach { example ->
            val chip = Chip(examplesViewHolder.itemView.context).apply {
                text = example
                setOnClickListener { onExampleClickListener(example) }
            }
            examplesViewHolder.viewBinding.chipGroup.addView(chip)
        }
    }

    override fun getItemCount() = examples.size

    inner class ExamplesViewHolder(
        val viewBinding: ExamplesLayoutBinding
    ) : RecyclerView.ViewHolder(viewBinding.root)
}
