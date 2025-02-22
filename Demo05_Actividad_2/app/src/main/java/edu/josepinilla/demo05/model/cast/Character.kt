package edu.josepinilla.demo05.model.cast


import com.google.gson.annotations.SerializedName

/**
 * data class Character
 * se encarga de almacenar la información de un personaje
 *
 * @author Jose Pinilla
 */
data class Character(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: Image?,
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)