package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class Links
 * se encarga de almacenar la información de los links
 *
 * @author Jose Pinilla
 */
data class Links(
    @SerializedName("nextepisode")
    val nextepisode: Nextepisode?,
    @SerializedName("previousepisode")
    val previousepisode: Previousepisode?,
    @SerializedName("self")
    val self: Self?
)