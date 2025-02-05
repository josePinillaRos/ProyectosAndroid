package edu.josepinilla.demo03.model

data class Items(
    var id: Int = 0,
    val title: String,
    val description: String,
    var image: String = ""
    ) {
    companion object {
        var identifier: Int = 0
        val items: MutableList<Items> = mutableListOf()
    }

    init {
        id = ++identifier
        image = "https://picsum.photos/200/200?image=$id"
    }
}