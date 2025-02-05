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

    //TODO
    fun archiveItem(item: Items) {
        val index = Items.items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            Items.items[index].archived = true
            Log.i("MainViewModel", "archiveItem: ${item.title} archivado")
        }
    }

    fun fetchArchivedItems(): MutableList<Items> {
        Log.i("MainViewModel", "fetchArchivedItems: ${Items.items.size}")
        return Items.items.filter { it.archived==true }.toMutableList()
    }
}