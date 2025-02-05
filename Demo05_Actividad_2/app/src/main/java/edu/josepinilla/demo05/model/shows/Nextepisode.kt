package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Nextepisode
 * se encarga de almacenar la información del siguiente episodio
 *
 * @author Jose Pinilla
 */
data class Nextepisode(
    @SerializedName("href")
    val href: String?,
    @SerializedName("name")
    val name: String?
)