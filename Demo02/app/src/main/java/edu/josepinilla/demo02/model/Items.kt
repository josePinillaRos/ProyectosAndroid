package edu.josepinilla.demo02.model

data class Items(
    val id: Int, val title: String, val description: String, val
    image: String
) {
    companion object {
        val items: MutableList<Items> = mutableListOf()

        init {
            for (i in 1..10) {
                items.add(
                    Items(
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
