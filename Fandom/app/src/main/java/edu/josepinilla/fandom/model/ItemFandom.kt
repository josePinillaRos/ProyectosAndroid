package edu.josepinilla.fandom.model

data class ItemFandom(val id: Int, val title: String, val description: String, val
image: String) {
    companion object {
        val items: MutableList<ItemFandom> = mutableListOf()
        init {
            for (i in 1..10) {
                items.add(
                    ItemFandom(
                        i,
                        "Item $i",
                        "Description for item $i",
                        "https://picsum.photos/200/200?image=$i"
                    )
                )
            }
        }
    }
}