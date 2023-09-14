package com.facebook.ta.domain

import com.facebook.ta.presentation.OlympViewModel

data class Olympic(
    val id: Int,
    val draw: Int,
    val name: String,
    val isExist: Boolean,
    val one: String = OlympViewModel.ONE_SIG,
    val app: String = OlympViewModel.APPS

)
