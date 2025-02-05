package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Previousepisode
 * se encarga de almacenar la información del episodio anterior
 *
 * @author Jose Pinilla
 */
data class Previousepisode(
    @SerializedName("href")
    val href: String?,
    @SerializedName("name")
    val name: String?
)