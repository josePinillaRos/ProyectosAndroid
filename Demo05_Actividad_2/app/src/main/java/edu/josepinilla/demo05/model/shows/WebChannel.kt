package edu.josepinilla.demo05.model.shows


import com.google.gson.annotations.SerializedName

/**
 * data class WebChannel
 * se encarga de almacenar la información del canal web
 *
 * @author Jose Pinilla
 */
data class WebChannel(
    @SerializedName("country")
    val country: Country?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("officialSite")
    val officialSite: String?
)