package com.example.mydailydriver.ui.elements

import androidx.annotation.StringRes
import com.example.mydailydriver.R

enum class Screens(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Notes(title = R.string.Notes)
}