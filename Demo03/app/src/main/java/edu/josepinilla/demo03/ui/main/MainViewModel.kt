package edu.josepinilla.demo03.ui.main

import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _fragmentShowed: String? = null
    val fragmentShowed: String?
    get() = _fragmentShowed

    fun setFragmentShowed(fragmentShowed: String) {
        _fragmentShowed = fragmentShowed
    }
}