package edu.josepinilla.demo05.model.cast


import com.google.gson.annotations.SerializedName

/**
 * data class Person
 * se encarga de almacenar la información de una persona
 *
 * @author Jose Pinilla
 */
data class Person(
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("country")
    val country: Country?,
    @SerializedName("deathday")
    val deathday: Any?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: Image?,
    @SerializedName("_links")
    val links: Links?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("updated")
    val updated: Int?,
    @SerializedName("url")
    val url: String?
)