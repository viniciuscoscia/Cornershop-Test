package com.cornershop.counterstest.presentation.main

import com.cornershop.counterstest.domain.usecases.GetCountersUseCase
import com.cornershop.counterstest.presentation.commons.BaseViewModel

class MainViewModel(
    private val countersUseCase: GetCountersUseCase
) : BaseViewModel() {


}