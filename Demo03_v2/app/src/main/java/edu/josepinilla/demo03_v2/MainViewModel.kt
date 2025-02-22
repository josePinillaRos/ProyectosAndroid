package edu.josepinilla.demo03_v2

import android.util.Log
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _fragmentShowed: String? = null
    val fragmentShowed: String?
        get() = _fragmentShowed

    fun setFragmentShowed(fragmentShowed: String) {
        _fragmentShowed = fragmentShowed
    }

    fun addItem(item: Items) {
        Items.items.add(item)
        Log.i("MainViewModel", "addItem: $item")
    }

    fun fetchItems(): MutableList<Items> {
        Log.i("MainViewModel", "fetchItems: ${Items.items.size}")
        return Items.items.filter { it.archived == false }.toMutableList()
    }
}