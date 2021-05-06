package com.cornershop.counterstest.presentation.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.FragmentMainBinding
import com.cornershop.counterstest.presentation.model.CounterUiModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {
    private val viewModel: MainViewModel by viewModel()
    private val viewBinding: FragmentMainBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        viewModel.countersLiveData.observe(viewLifecycleOwner) { counters ->
            setupRecyclerView(counters)
        }
    }

    private fun setupRecyclerView(counters: List<CounterUiModel>?) {

    }
}