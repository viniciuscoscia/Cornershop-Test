package com.cornershop.counterstest.presentation.ui.main.counterexamples

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentCounterExamplesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CounterExamplesFragment : Fragment(R.layout.fragment_counter_examples) {
    private val viewModel: CounterExamplesViewModel by viewModel()
    private val viewBinding: FragmentCounterExamplesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLivedataObserver()
    }

    private fun setupLivedataObserver() {
        viewModel.examplesLiveData.observe(viewLifecycleOwner) { examples ->
            viewBinding.examplesRecyclerView.adapter = ExamplesAdapter(examples) { example ->
                onExampleClicked(example)
            }
        }
    }

    private fun onExampleClicked(example: String) {
        findNavController()
            .navigate(
                CounterExamplesFragmentDirections.actionCounterExamplesFragmentToCreateCounterFragment(
                    example
                )
            )
    }
}